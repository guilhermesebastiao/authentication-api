package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.adapter.out.security.adapter.SecureHasherAdapter;
import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.SecretsProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecureHasherAdapterTest {

    @Test
    @DisplayName("Should generate the configured HMAC-SHA256 as lowercase hexadecimal")
    void givenValueAndHmacSecret_whenHash_thenReturnHexadecimalDigest() {
        SecretsProperties properties = new SecretsProperties(
                "authentication-api",
                "access-token-secret",
                "test-hmac-secret"
        );

        SecureHasherAdapter adapter = new SecureHasherAdapter(properties);

        String result = adapter.hash("account@example.com");

        assertEquals(
                "9f420163bb992ba5282a677c8e77bcedd06b26d3c43582648b869412f5b6b950",
                result
        );

        assertEquals(64, result.length());
        assertTrue(result.matches("[0-9a-f]+"));
    }
}
