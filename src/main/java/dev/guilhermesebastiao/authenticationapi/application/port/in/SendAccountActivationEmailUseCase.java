package dev.guilhermesebastiao.authenticationapi.application.port.in;

import java.time.Instant;

public interface SendAccountActivationEmailUseCase {
    void execute(String email, String otpCode, Instant expiresAt);
}
