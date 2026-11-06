package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;

public interface GetAccountActivationUseCase {
    AccountActivationOutput execute(String activationToken);
}
