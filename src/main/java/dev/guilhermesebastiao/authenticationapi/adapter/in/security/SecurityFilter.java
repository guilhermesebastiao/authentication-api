package dev.guilhermesebastiao.authenticationapi.adapter.in.security;

import dev.guilhermesebastiao.authenticationapi.adapter.out.security.SecurityAccount;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.SecurityAccountService;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ValidateSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.JwtTokenPort;
import dev.guilhermesebastiao.authenticationapi.application.result.JwtValidationResult;
import dev.guilhermesebastiao.authenticationapi.domain.exception.DomainException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.JwtValidationException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.UnauthorizedException;
import dev.guilhermesebastiao.authenticationapi.infrastructure.config.ApiRoutesConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String SESSION_TOKEN_HEADER = "X-Session";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final ValidateSessionUseCase validateSessionUseCase;
    private final HandlerExceptionResolver exceptionResolver;
    private final SecurityAccountService userDetailsService;
    private final JwtTokenPort jwtToken;

    public SecurityFilter(
            @Qualifier("handlerExceptionResolver")
            HandlerExceptionResolver exceptionResolver,
            ValidateSessionUseCase validateSessionUseCase,
            SecurityAccountService userDetailsService,
            JwtTokenPort jwtToken
    ) {
        this.validateSessionUseCase = validateSessionUseCase;
        this.userDetailsService = userDetailsService;
        this.exceptionResolver = exceptionResolver;
        this.jwtToken = jwtToken;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getServletPath();

        return Arrays.stream(ApiRoutesConfig.PUBLIC).anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            authenticate(request);
        } catch (DomainException exception) {
            SecurityContextHolder.clearContext();
            exceptionResolver.resolveException(request, response, null, exception);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        String sessionToken = getSessionToken(request);

        if (accessToken == null || sessionToken == null) {
            throw new UnauthorizedException();
        }

        JwtValidationResult jwtResult = jwtToken.validate(accessToken, sessionToken);

        if (!jwtResult.isValid()) {
            throw new JwtValidationException(jwtResult.status());
        }

        validateSessionUseCase.execute(sessionToken);

        SecurityAccount user = userDetailsService.loadUserById(jwtResult.userId());

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }

        String token = authorization.substring(BEARER_PREFIX.length()).trim();

        return token.isEmpty() ? null : token;
    }

    private String getSessionToken(HttpServletRequest request) {
        String token = request.getHeader(SESSION_TOKEN_HEADER);

        if (token == null || token.isBlank()) {
            return null;
        }

        return token.trim();
    }
}
