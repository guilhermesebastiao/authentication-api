package dev.guilhermesebastiao.authenticationapi.application.dto.output;

import dev.guilhermesebastiao.authenticationapi.domain.enums.JwtValidationStatus;

import java.util.UUID;

public record JwtValidationOutput(JwtValidationStatus status, UUID userId) {
    public static JwtValidationOutput valid(UUID userId) {
        return new JwtValidationOutput(JwtValidationStatus.VALID, userId);
    }

    public static JwtValidationOutput invalid(JwtValidationStatus status) {
        return new JwtValidationOutput(status, null);
    }

    public boolean isValid() {
        return status == JwtValidationStatus.VALID;
    }
}
