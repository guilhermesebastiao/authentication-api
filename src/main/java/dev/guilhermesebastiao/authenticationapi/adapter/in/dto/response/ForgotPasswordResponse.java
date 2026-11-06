package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response;

import java.time.Instant;

public record ForgotPasswordResponse(
        String email,
        String forgotToken,
        Instant resendAvailableAt,
        Instant expiresAt
) {
}
