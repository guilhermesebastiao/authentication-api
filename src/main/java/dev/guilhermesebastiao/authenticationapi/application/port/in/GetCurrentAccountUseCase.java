package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface GetCurrentAccountUseCase {
    AccountOutput execute(Account account);
}
