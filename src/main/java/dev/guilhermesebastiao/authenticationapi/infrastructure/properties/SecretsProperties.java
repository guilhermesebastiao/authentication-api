package dev.guilhermesebastiao.authenticationapi.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.secrets")
public record SecretsProperties(
        String issuer,
        String accessToken,
        String hmac
) {
}
