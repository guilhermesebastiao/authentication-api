package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.RefreshTokenInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AuthOutput;

public interface RefreshTokenUseCase {
    AuthOutput execute(RefreshTokenInput input);
}
