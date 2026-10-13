package br.com.guisebastiao.authenticationapi.adapter.in.dto.response;

import java.time.Instant;
import java.util.UUID;

public record AccountActivationResponse(
        String email,
        String activationToken,
        Instant expiresAt,
        Instant resendAvailableAt
) {
}
