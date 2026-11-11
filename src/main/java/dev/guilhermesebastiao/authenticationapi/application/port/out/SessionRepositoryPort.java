package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageResult;
import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageQuery;
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

    PageResult<Session> findAllByAccountIdAndNotRevoked(UUID accountId,  String sessionTokenHash, PageQuery pagination);

    List<Session> findAllByAccountIdAndNotRevoked(UUID accountId);

    Optional<Session> findByAccountIdAndSessionTokenHash(UUID userId, String sessionTokenHash);

    List<Session> findAllByAccountIdAndSessionIdsAndNotRevoked(UUID accountId, List<UUID> sessionIds);

    long deleteAllExpiredOrUsed(Instant now);
}
