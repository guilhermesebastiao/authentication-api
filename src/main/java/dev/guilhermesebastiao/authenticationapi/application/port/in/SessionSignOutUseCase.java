package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

import java.util.List;
import java.util.UUID;

public interface SessionSignOutUseCase {
    void execute(Account account, List<UUID> sessionsIds);
}
