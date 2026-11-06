package dev.guilhermesebastiao.authenticationapi.adapter.out.security.adapter;

import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.SecretsProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HexFormat;

@Component
public class SecureHasherAdapter implements SecureHasherPort {
    private static final String ALGORITHM = "HmacSHA256";
    private final SecretKeySpec secretKey;

    public SecureHasherAdapter(SecretsProperties properties) {
        this.secretKey = new SecretKeySpec(
                properties.hmac().getBytes(StandardCharsets.UTF_8),
                ALGORITHM
        );
    }

    @Override
    public String hash(String value) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);

            byte[] hashedValue = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashedValue);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Failed to generate deterministic hash", exception);
        }
    }
}
