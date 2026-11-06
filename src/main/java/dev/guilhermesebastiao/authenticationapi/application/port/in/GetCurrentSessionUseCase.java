package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface GetCurrentSessionUseCase {
    SessionOutput execute(Account account, String currentSession);
}
