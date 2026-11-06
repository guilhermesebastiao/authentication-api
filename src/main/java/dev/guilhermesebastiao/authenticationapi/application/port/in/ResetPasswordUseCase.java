package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.ResetPasswordCommand;

public interface ResetPasswordUseCase {
    void execute(ResetPasswordCommand command);
}
