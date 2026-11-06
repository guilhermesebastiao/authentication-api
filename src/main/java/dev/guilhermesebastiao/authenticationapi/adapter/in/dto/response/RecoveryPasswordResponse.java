package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response;

import java.time.Instant;

public record RecoveryPasswordResponse(
        String recoveryToken,
        Instant expiresAt,
        Boolean usedAt
) {
}
