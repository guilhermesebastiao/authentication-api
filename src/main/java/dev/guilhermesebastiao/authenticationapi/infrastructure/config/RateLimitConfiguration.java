package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import dev.guilhermesebastiao.authenticationapi.adapter.in.security.RateLimitPolicy;
import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.RateLimitProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Configuration
public class RateLimitConfiguration {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final RateLimitProperties properties;

    public RateLimitConfiguration(RateLimitProperties properties) {
        this.properties = properties;
    }

    public BucketConfiguration globalBucketConfiguration() {
        RateLimitProperties.Global global = properties.global();
        return bucketConfiguration(global.capacity(), global.refillTokens(), global.duration());
    }

    public Optional<RateLimitPolicy> policyFor(String path) {
        return policies().stream()
                .filter(policy -> PATH_MATCHER.match(policy.path(), path))
                .findFirst();
    }

    public BucketConfiguration bucketConfiguration(RateLimitPolicy policy) {
        return bucketConfiguration(policy.capacity(), policy.refillTokens(), policy.refillDuration());
    }

    public List<RateLimitPolicy> policies() {
        return properties.policies() == null ? List.of() : properties.policies();
    }

    private BucketConfiguration bucketConfiguration(long capacity, long refillTokens, Duration refillDuration) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(capacity)
                .refillGreedy(refillTokens, refillDuration)
                .build();

        return BucketConfiguration.builder().addLimit(limit).build();
    }
}
