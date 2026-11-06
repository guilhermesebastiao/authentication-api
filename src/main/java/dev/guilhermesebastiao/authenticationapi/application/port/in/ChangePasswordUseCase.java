package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.command.ChangePasswordCommand;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface ChangePasswordUseCase {
    void execute(Account account, ChangePasswordCommand command);
}
