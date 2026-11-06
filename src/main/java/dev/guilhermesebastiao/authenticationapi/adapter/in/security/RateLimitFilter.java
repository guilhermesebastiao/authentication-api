package dev.guilhermesebastiao.authenticationapi.adapter.in.security;

import dev.guilhermesebastiao.authenticationapi.domain.exception.RateLimitExceededException;
import dev.guilhermesebastiao.authenticationapi.infrastructure.config.RateLimitConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final String GLOBAL_KEY_PREFIX = "rate-limit:global:";
    private static final String POLICY_KEY_PREFIX = "rate-limit:";

    private final ProxyManager<String> proxyManager;
    private final RateLimitConfiguration rateLimitConfiguration;
    private final HandlerExceptionResolver exceptionResolver;

    public RateLimitFilter(
            ProxyManager<String> proxyManager,
            RateLimitConfiguration rateLimitConfiguration,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver
    ) {
        this.proxyManager = proxyManager;
        this.rateLimitConfiguration = rateLimitConfiguration;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        String redisIp = ip.replace(':', '_');

        try {
            rejectIfExceeded(proxyManager.builder().build(
                    GLOBAL_KEY_PREFIX + redisIp,
                    rateLimitConfiguration.globalBucketConfiguration()
            ));

            rateLimitConfiguration.policyFor(request.getServletPath())
                    .ifPresent(policy -> rejectIfExceeded(proxyManager.builder().build(
                            policyKey(policy, redisIp),
                            rateLimitConfiguration.bucketConfiguration(policy)
                    )));
        } catch (RateLimitExceededException exception) {
            exceptionResolver.resolveException(request, response, null, exception);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void rejectIfExceeded(BucketProxy bucket) {
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (!probe.isConsumed()) {
            long retryAfterSeconds = Math.max(1, (probe.getNanosToWaitForRefill() + 999_999_999L) / 1_000_000_000L);
            throw new RateLimitExceededException(retryAfterSeconds);
        }
    }

    private String policyKey(RateLimitPolicy policy, String ip) {
        String group = policy.path().substring(1, policy.path().length() - 3);
        return POLICY_KEY_PREFIX + group + ":" + ip;
    }
}
