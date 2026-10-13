package br.com.guisebastiao.authenticationapi.application.result;

import java.time.Instant;
import java.util.UUID;

public record RecoverPasswordResult(
        String email,
        String recoverToken,
        Instant resendAvailableAt,
        Instant expiresAt
) {
}
