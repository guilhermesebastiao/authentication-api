package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.SignOutUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionRevokedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.time.Instant;
import java.util.List;

public class SignOutService implements SignOutUseCase {
    private final SessionRepositoryPort sessionRepository;
    private final RefreshRepositoryPort refreshRepository;
    private final SecureHasherPort secureHasher;

    public SignOutService(
            SessionRepositoryPort sessionRepository,
            RefreshRepositoryPort refreshRepository,
            SecureHasherPort secureHasher
    ) {
        this.sessionRepository = sessionRepository;
        this.refreshRepository = refreshRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public void execute(Account account, String sessionToken) {
        consumeSignOut(account, sessionToken);
    }

    private void consumeSignOut(Account account, String sessionToken) {
        String sessionTokenHash = secureHasher.hash(sessionToken);

        Session session = sessionRepository.findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash)
                .orElseThrow(SessionNotFoundException::new);

        validateSession(session);

        List<Refresh> refreshes = refreshRepository.findAllBySessionIdsAndNotRevoked(List.of(session.getId()));

        refreshes.forEach((refresh) -> refresh.setRevokedAt(Instant.now()));

        refreshRepository.save(refreshes);

        session.setRevokedAt(Instant.now());

        sessionRepository.save(session);
    }

    private void validateSession(Session session) {
        if (!session.getExpiresAt().isAfter(Instant.now())) {
            throw new SessionExpiredException();
        }

        if (session.getRevokedAt() != null) {
            throw new SessionRevokedException();
        }
    }
}
