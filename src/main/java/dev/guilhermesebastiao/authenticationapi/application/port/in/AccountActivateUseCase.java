package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.AccountActivationCommand;

public interface AccountActivateUseCase {
    void execute(AccountActivationCommand command);
}
