package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class RevokeAllSessionsService implements RevokeAllSessionsUseCase {
    private final SessionRepositoryPort sessionRepository;
    private final RefreshRepositoryPort refreshRepository;

    public RevokeAllSessionsService(
            SessionRepositoryPort sessionRepository,
            RefreshRepositoryPort refreshRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void execute(Account account) {
        Instant revokedAt = Instant.now();

        List<Session> sessions = sessionRepository.findAllByAccountIdAndNotRevoked(account.getId());

        if (sessions.isEmpty()) {
            return;
        }

        List<UUID> sessionIds = sessions.stream()
                .map(Session::getId)
                .toList();

        List<Refresh> refreshes = refreshRepository.findAllBySessionIdsAndNotRevoked(sessionIds);

        refreshes.forEach(refresh -> refresh.setRevokedAt(revokedAt));

        sessions.forEach(session -> session.setRevokedAt(revokedAt));

        if (!refreshes.isEmpty()) {
            refreshRepository.save(refreshes);
        }

        sessionRepository.save(sessions);
    }
}
