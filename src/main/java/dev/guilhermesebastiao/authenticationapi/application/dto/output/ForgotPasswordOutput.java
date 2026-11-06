package dev.guilhermesebastiao.authenticationapi.application.dto.output;

import java.time.Instant;

public record ForgotPasswordOutput(
        String email,
        String forgotToken,
        Instant resendAvailableAt,
        Instant expiresAt
) {
}
