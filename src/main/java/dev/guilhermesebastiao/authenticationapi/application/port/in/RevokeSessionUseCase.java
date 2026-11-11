package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

import java.util.UUID;

public interface RevokeSessionUseCase {
    void execute(Account account, String sessionToken);

    void execute(Account account, UUID sessionId);
}
