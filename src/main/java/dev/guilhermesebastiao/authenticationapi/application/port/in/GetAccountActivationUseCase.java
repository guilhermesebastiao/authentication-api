package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.AccountActivationResult;

public interface GetAccountActivationUseCase {
    AccountActivationResult execute(String activationToken);
}
