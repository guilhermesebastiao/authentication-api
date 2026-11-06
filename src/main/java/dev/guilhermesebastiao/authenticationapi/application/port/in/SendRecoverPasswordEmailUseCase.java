package dev.guilhermesebastiao.authenticationapi.application.port.in;

import java.time.Instant;

public interface SendRecoverPasswordEmailUseCase {
    void execute(String email, String otpCode, Instant expiresAt);
}
