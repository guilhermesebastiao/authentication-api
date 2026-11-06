package dev.guilhermesebastiao.authenticationapi.application.result;

import dev.guilhermesebastiao.authenticationapi.domain.enums.JwtValidationStatus;

import java.util.UUID;

public record JwtValidationResult(JwtValidationStatus status, UUID userId) {
    public static JwtValidationResult valid(UUID userId) {
        return new JwtValidationResult(JwtValidationStatus.VALID, userId);
    }

    public static JwtValidationResult invalid(JwtValidationStatus status) {
        return new JwtValidationResult(status, null);
    }

    public boolean isValid() {
        return status == JwtValidationStatus.VALID;
    }
}
