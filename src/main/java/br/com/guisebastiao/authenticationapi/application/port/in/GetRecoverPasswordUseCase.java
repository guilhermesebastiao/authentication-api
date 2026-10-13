package br.com.guisebastiao.authenticationapi.application.port.in;

import br.com.guisebastiao.authenticationapi.application.result.RecoverPasswordResult;

public interface GetRecoverPasswordUseCase {
    RecoverPasswordResult execute(String recoverToken);
}
