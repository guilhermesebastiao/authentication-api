package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface CreateAccountActivationUseCase {
    AccountActivationOutput execute(Account account);
}
