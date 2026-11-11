package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageResult;
import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageQuery;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetDeviceNameUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.util.Comparator;
import java.util.List;

public class GetSessionsService implements GetSessionsUseCase {
    private final GetDeviceNameUseCase getDeviceNameUseCase;
    private final SessionRepositoryPort sessionRepository;
    private final SecureHasherPort secureHasher;

    public GetSessionsService(
            GetDeviceNameUseCase getDeviceNameUseCase,
            SessionRepositoryPort sessionRepository,
            SecureHasherPort secureHasher
    ) {
        this.getDeviceNameUseCase = getDeviceNameUseCase;
        this.sessionRepository = sessionRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public PageResult<SessionOutput> execute(Account account, String currentSession, PageQuery paging) {
        if (currentSession.isBlank()) {
            throw new SessionNotFoundException();
        }


        String sessionTokenHash = secureHasher.hash(currentSession);

        PageResult<Session> sessions = sessionRepository.findAllByAccountIdAndNotRevoked(
                account.getId(),
                sessionTokenHash,
                paging
        );


        List<SessionOutput> content = sessions.content()
                .stream()
                .map((session) -> new SessionOutput(
                        session.getId(),
                        getDeviceNameUseCase.execute(session.getUserAgent()),
                        session.getType(),
                        session.getLastSeenAt(),
                        session.getLocation(),
                        session.getIpAddress(),
                        sessionTokenHash.equals(session.getSessionTokenHash()),
                        session.getCreatedAt()
                ))
                .toList();

        return new PageResult<>(
                content,
                sessions.totalItems(),
                sessions.totalPages(),
                paging.page(),
                paging.size()
        );
    }
}
