package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.CreateRecoverPasswordCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.RecoverPasswordResult;

public interface CreateRecoverPasswordUseCase {
    RecoverPasswordResult execute(CreateRecoverPasswordCommand command);
}
