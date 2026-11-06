package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;

public interface ResendAccountActivationUseCase {
    AccountActivationOutput execute(String activationToken);
}
