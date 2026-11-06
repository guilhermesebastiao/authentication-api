package dev.guilhermesebastiao.authenticationapi.application.dto.output;

import java.time.Instant;

public record AccountActivationOutput(
        String email,
        String activationToken,
        Instant expiresAt,
        Instant resendAvailableAt
) {
}
