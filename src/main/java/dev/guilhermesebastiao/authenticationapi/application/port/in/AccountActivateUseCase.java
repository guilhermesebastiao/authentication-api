package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.AccountActivationInput;

public interface AccountActivateUseCase {
    void execute(AccountActivationInput input);
}
