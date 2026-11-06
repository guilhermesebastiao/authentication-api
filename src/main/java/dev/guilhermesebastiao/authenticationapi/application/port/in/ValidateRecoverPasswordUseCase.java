package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.ValidateRecoverPasswordCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.RecoverPasswordResult;

public interface ValidateRecoverPasswordUseCase {
    RecoverPasswordResult execute(ValidateRecoverPasswordCommand command);
}
