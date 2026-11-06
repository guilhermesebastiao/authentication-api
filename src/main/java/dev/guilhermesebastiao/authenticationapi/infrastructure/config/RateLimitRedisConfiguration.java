package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import dev.guilhermesebastiao.authenticationapi.adapter.in.security.RateLimitPolicy;
import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.RateLimitProperties;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.Comparator;

@Configuration
public class RateLimitRedisConfiguration {
    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, byte[]> rateLimitRedisConnection(
            LettuceConnectionFactory connectionFactory
    ) {
        RedisClient redisClient = (RedisClient) connectionFactory.getNativeClient();
        RedisCodec<String, byte[]> codec = RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE);
        return redisClient.connect(codec);
    }

    @Bean
    public ProxyManager<String> rateLimitProxyManager(
            StatefulRedisConnection<String, byte[]> rateLimitRedisConnection,
            RateLimitProperties properties
    ) {
        return LettuceBasedProxyManager.builderFor(rateLimitRedisConnection)
                .withExpirationStrategy(ExpirationAfterWriteStrategy
                        .basedOnTimeForRefillingBucketUpToMax(maxRefillDuration(properties)))
                .build();
    }

    private Duration maxRefillDuration(RateLimitProperties properties) {
        Duration policyDuration = properties.policies() == null
                ? Duration.ZERO
                : properties.policies().stream()
                .map(RateLimitPolicy::refillDuration)
                .max(Comparator.naturalOrder())
                .orElse(Duration.ZERO);

        return properties.global().duration().compareTo(policyDuration) > 0
                ? properties.global().duration()
                : policyDuration;
    }
}
