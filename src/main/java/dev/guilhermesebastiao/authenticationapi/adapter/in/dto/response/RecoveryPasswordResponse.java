package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response;

import java.time.Instant;

public record RecoveryPasswordResponse(
        String recoveryToken,
        String email,
        Instant expiresAt,
        Boolean usedAt
) {
}
