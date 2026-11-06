package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.AuthOutput;

public interface GoogleSignInUseCase {
    AuthOutput execute(String code, String userAgent, String ipAddress);
}
