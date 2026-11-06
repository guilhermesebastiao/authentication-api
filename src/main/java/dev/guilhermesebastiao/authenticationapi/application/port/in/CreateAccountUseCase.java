package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.CreateAccountCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.AccountActivationResult;

public interface CreateAccountUseCase {
    AccountActivationResult execute(CreateAccountCommand command);
}
