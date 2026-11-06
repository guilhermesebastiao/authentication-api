package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.SessionSignOutUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class SessionSignOutService implements SessionSignOutUseCase {
    private final SessionRepositoryPort sessionRepository;
    private final RefreshRepositoryPort refreshRepository;

    public SessionSignOutService(
            SessionRepositoryPort sessionRepository,
            RefreshRepositoryPort refreshRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void execute(Account account, List<UUID> sessionsIds) {
        List<Session> sessions = sessionRepository.findAllByAccountIdAndSessionIdsAndNotRevoked(
                account.getId(),
                sessionsIds
        );

        if (sessions.isEmpty()) {
            return;
        }

        List<UUID> authorizedSessionIds = sessions.stream()
                .map(Session::getId)
                .toList();

        List<Refresh> refreshes = refreshRepository.findAllBySessionIdsAndNotRevoked(authorizedSessionIds);

        Instant revokedAt = Instant.now();

        refreshes.forEach((refresh) -> refresh.setRevokedAt(revokedAt));
        sessions.forEach((session) -> session.setRevokedAt(revokedAt));

        refreshRepository.save(refreshes);
        sessionRepository.save(sessions);
    }
}
