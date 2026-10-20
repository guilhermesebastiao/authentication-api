package br.com.guisebastiao.authenticationapi.adapter.in.dto.response;

import java.time.Instant;
import java.util.UUID;

public record RecoverPasswordResponse(
        String email,
        String recoverToken,
        Instant resendAvailableAt,
        Instant expiresAt,
        boolean isVerified
) {
}
