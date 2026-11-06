package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.SignInInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AuthOutput;

public interface SignInUseCase {
    AuthOutput execute(SignInInput input, String userAgent, String ipAddress);
}
