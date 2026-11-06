package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.result.PageResult;
import dev.guilhermesebastiao.authenticationapi.application.command.PageQueryCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.result.SessionResult;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.util.List;

public class GetSessionsService implements GetSessionsUseCase {
    private final SessionRepositoryPort sessionRepository;
    private final SecureHasherPort secureHasher;

    public GetSessionsService(
            SessionRepositoryPort sessionRepository,
            SecureHasherPort secureHasher
    ) {
        this.sessionRepository = sessionRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public PageResult<SessionResult> execute(Account account, String currentSession, PageQueryCommand paging) {
        if (currentSession.isBlank()) {
            throw new SessionNotFoundException();
        }

        PageResult<Session> sessions = sessionRepository.findAllByAccountIdAndNotRevoked(account.getId(), paging);

        String sessionTokenHash = secureHasher.hash(currentSession);

        List<SessionResult> content = sessions.content()
                .stream()
                .map((session) -> new SessionResult(
                        session.getId(),
                        session.getType(),
                        session.getLastSeenAt(),
                        session.getLocation(),
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
