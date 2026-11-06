package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.RefreshTokenCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.AuthResult;

public interface RefreshTokenUseCase {
    AuthResult execute(RefreshTokenCommand command);
}
