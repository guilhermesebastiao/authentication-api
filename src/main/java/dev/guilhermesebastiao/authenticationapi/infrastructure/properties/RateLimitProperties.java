package dev.guilhermesebastiao.authenticationapi.infrastructure.properties;

import dev.guilhermesebastiao.authenticationapi.adapter.in.security.RateLimitPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "app.rate-limit")
public record RateLimitProperties(
        Global global,
        List<RateLimitPolicy> policies
) {
    public record Global(
            long capacity,
            long refillTokens,
            Duration duration
    ) {
    }
}
