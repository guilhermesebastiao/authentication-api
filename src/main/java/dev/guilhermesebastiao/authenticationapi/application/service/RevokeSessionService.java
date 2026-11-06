package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RefreshTokenNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.time.Instant;

public class RevokeSessionService implements RevokeSessionUseCase {
    private final SessionRepositoryPort sessionRepository;
    private final RefreshRepositoryPort refreshRepository;
    private final SecureHasherPort secureHasher;

    public RevokeSessionService(
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
        String sessionTokenHash = secureHasher.hash(sessionToken);

        Session session = sessionRepository.findBySessionTokenHash(sessionTokenHash)
                .orElseThrow(SessionNotFoundException::new);

        Refresh refresh = refreshRepository.findByRefreshBySessionId(session.getId())
                .orElseThrow(RefreshTokenNotFoundException::new);

        Instant now = Instant.now();

        refresh.setRevokedAt(now);

        refreshRepository.save(refresh);

        session.setRevokedAt(now);

        sessionRepository.save(session);
    }
}
