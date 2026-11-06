package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.RecoverPasswordResult;

public interface ResendRecoverPasswordEmailUseCase {
    RecoverPasswordResult execute(String recoverToken);
}
