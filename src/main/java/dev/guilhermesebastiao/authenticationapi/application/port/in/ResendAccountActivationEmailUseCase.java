package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.AccountActivationResult;

public interface ResendAccountActivationEmailUseCase {
    AccountActivationResult execute(String activationToken);
}
