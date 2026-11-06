package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.AccountActivationResult;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface CreateAccountActivationUseCase {
    AccountActivationResult execute(Account account);
}
