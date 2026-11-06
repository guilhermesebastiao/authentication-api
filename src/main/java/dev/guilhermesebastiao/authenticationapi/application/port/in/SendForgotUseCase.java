package dev.guilhermesebastiao.authenticationapi.application.port.in;

import java.time.Instant;

public interface SendForgotUseCase {
    void execute(String email, String otp, Instant expiresAt);
}
