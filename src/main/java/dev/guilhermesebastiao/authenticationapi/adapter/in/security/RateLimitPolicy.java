package dev.guilhermesebastiao.authenticationapi.adapter.in.security;

import java.time.Duration;

public record RateLimitPolicy(
        String path,
        long capacity,
        long refillTokens,
        Duration refillDuration
) {
}
