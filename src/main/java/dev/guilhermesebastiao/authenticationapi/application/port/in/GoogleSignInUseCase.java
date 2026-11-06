package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.GoogleSignInCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.AuthResult;

public interface GoogleSignInUseCase {
    AuthResult execute(GoogleSignInCommand command, String userAgent, String ipAddress);
}
