package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.RecoverPasswordResult;

public interface GetRecoverPasswordUseCase {
    RecoverPasswordResult execute(String recoverToken);
}
