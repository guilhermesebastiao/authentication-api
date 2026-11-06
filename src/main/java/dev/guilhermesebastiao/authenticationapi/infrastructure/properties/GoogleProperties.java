package dev.guilhermesebastiao.authenticationapi.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.google")
public record GoogleProperties(
        String clientId,
        String clientSecret
) {
}
