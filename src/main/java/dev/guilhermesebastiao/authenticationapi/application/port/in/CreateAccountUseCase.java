package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.CreateAccountInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;

public interface CreateAccountUseCase {
    AccountActivationOutput execute(CreateAccountInput input);
}
