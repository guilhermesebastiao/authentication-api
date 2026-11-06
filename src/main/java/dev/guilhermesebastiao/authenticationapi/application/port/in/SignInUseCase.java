package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.SignInCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.AuthResult;

public interface SignInUseCase {
    AuthResult execute(SignInCommand command, String userAgent, String ipAddress);
}
