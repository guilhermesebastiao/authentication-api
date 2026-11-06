package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface SignOutAllUseCase {
    void execute(Account account);
}
