package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateSessionOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface CreateSessionUseCase {
    CreateSessionOutput execute(Account account, String userAgent, String ipAddress);
}
