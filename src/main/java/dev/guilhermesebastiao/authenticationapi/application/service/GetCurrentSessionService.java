package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.GetCurrentSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetDeviceNameUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotBelongToAccountException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

public class GetCurrentSessionService implements GetCurrentSessionUseCase {
    private final GetDeviceNameUseCase getDeviceNameUseCase;
    private final SessionRepositoryPort sessionRepository;
    private final SecureHasherPort secureHasher;

    public GetCurrentSessionService(
            GetDeviceNameUseCase getDeviceNameUseCase,
            SessionRepositoryPort sessionRepository,
            SecureHasherPort secureHasher
    ) {
        this.getDeviceNameUseCase = getDeviceNameUseCase;
        this.sessionRepository = sessionRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public SessionOutput execute(Account account, String currentSession) {
        String sessionTokenHash = secureHasher.hash(currentSession);

        Session session = sessionRepository.findBySessionTokenHash(sessionTokenHash)
                .orElseThrow(SessionNotFoundException::new);

        if (!session.getAccount().getId().equals(account.getId())) {
            throw new SessionNotBelongToAccountException();
        }

        String name = getDeviceNameUseCase.execute(session.getUserAgent());

        return new SessionOutput(
                session.getId(),
                name,
                session.getType(),
                session.getLastSeenAt(),
                session.getLocation(),
                session.getIpAddress(),
                true,
                session.getCreatedAt()
        );
    }
}
