package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.SessionResult;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface GetCurrentSessionUseCase {
    SessionResult execute(Account account, String currentSession);
}
