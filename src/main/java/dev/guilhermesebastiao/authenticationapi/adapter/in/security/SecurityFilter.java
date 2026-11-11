package dev.guilhermesebastiao.authenticationapi.adapter.in.security;

import dev.guilhermesebastiao.authenticationapi.adapter.out.security.context.SecurityAccount;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.context.SecurityAccountService;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ValidateSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.JwtTokenPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.JwtValidationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.exception.DomainException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.JwtValidationException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.UnauthorizedException;
import dev.guilhermesebastiao.authenticationapi.infrastructure.config.ApiRoutesConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private static final String SESSION_TOKEN_HEADER = "X-Session";
    private static final String BEARER_PREFIX = "Bearer ";

    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver exceptionResolver;

    private final ValidateSessionUseCase validateSessionUseCase;
    private final SecurityAccountService userDetailsService;
    private final JwtTokenPort jwtToken;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return HttpMethod.OPTIONS.matches(request.getMethod())
                || ApiRoutesConfig.isPublic(request.getServletPath());
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

        JwtValidationOutput jwtResult = jwtToken.validate(accessToken, sessionToken);

        if (!jwtResult.isValid()) {
            throw new JwtValidationException(jwtResult.status());
        }

        validateSessionUseCase.execute(sessionToken);

        SecurityAccount user = userDetailsService.loadUserById(jwtResult.userId());

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

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
