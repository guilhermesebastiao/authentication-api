package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.CreateSessionResult;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface CreateSessionUseCase {
    CreateSessionResult execute(Account account, String userAgent, String ipAddress);
}
