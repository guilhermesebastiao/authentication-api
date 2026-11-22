package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageBody;
import dev.guilhermesebastiao.authenticationapi.application.dto.query.PagingParam;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepositoryPort {
    Session save(Session session);

    void save(List<Session> sessions);

    Optional<Session> findById(UUID id);

    Optional<Session> findBySessionTokenHash(String sessionTokenHash);

    PageBody<Session> findAllByAccountIdAndNotRevoked(UUID accountId, String sessionTokenHash, PagingParam pagination);

    List<Session> findAllByAccountIdAndNotRevoked(UUID accountId);

    Optional<Session> findByAccountIdAndSessionTokenHash(UUID userId, String sessionTokenHash);

    List<Session> findAllByAccountIdAndSessionIdsAndNotRevoked(UUID accountId, List<UUID> sessionIds);

    long deleteAllExpiredOrUsed(Instant now);
}
