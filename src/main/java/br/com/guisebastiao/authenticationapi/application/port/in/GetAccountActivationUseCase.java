package br.com.guisebastiao.authenticationapi.application.port.in;

import br.com.guisebastiao.authenticationapi.application.result.AccountActivationResult;

public interface GetAccountActivationUseCase {
    AccountActivationResult execute(String activationToken);
}
