package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.DisableAccountInput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface DisableAccountUseCase {
    void execute(Account account, DisableAccountInput input);
}
