package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface SignOutUseCase {
    void execute(Account account, String sessionToken);
}
