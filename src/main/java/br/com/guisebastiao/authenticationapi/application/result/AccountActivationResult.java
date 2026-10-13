package br.com.guisebastiao.authenticationapi.application.result;

import java.time.Instant;
import java.util.UUID;

public record AccountActivationResult(
        String email,
        String activationToken,
        Instant expiresAt,
        Instant resendAvailableAt
) {
}
