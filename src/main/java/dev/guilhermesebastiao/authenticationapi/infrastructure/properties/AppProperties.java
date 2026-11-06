package dev.guilhermesebastiao.authenticationapi.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        String frontendUrl,
        String emailFrom
) {
}
