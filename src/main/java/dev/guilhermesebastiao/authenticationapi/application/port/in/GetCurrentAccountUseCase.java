package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.AccountResult;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface GetCurrentAccountUseCase {
    AccountResult execute(Account account);
}
