package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.RecoveryPasswordOutput;

public interface GetRecoveryPasswordUseCase {
    RecoveryPasswordOutput execute(String recoveryToken);
}
