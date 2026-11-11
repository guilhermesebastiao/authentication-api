package dev.guilhermesebastiao.authenticationapi.application.dto.output;

import java.time.Instant;

public record RecoveryPasswordOutput(
        String recoveryToken,
        String email,
        Instant expiresAt,
        Boolean usedAt
) {
}
