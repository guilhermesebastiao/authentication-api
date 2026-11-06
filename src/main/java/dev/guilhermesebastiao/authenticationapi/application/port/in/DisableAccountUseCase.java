package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.DisableAccountCommand;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface DisableAccountUseCase {
    void execute(Account account, DisableAccountCommand command);
}
