package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.ValidateSessionUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.*;
import br.com.guisebastiao.authenticationapi.domain.exception.*;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionRevokedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.time.Instant;

public class ValidateSessionService implements ValidateSessionUseCase {
    private final SessionRepositoryPort sessionRepository;
    private final SecureHasherPort secureHasher;

    public ValidateSessionService(
            SessionRepositoryPort sessionRepository,
            SecureHasherPort secureHasher
    ) {
        this.sessionRepository = sessionRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public Session execute(String sessionToken) {
        String sessionTokenHash = secureHasher.hash(sessionToken);

        Session session = sessionRepository.findBySessionTokenHash(sessionTokenHash)
                .orElseThrow(SessionNotFoundException::new);

        if (session.getRevokedAt() != null) {
            throw new SessionRevokedException();
        }

        if (!session.getExpiresAt().isAfter(Instant.now())) {
            throw new SessionExpiredException();
        }

        return session;
    }
}
