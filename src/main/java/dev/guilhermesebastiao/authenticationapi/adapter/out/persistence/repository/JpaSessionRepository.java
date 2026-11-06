package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.SessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaSessionRepository extends JpaRepository<SessionEntity, UUID> {
    Optional<SessionEntity> findBySessionTokenHash(String sessionTokenHash);

    Optional<SessionEntity> findByAccountIdAndSessionTokenHash(UUID accountId, String sessionTokenHash);

    @Query("""
         SELECT s FROM SessionEntity s
        WHERE s.account.id = :accountId
         AND s.revokedAt IS NULL
    """)
    Page<SessionEntity> findAllByAccountIdAndNotRevoked(
            @Param("accountId") UUID accountId,
            Pageable pageable
    );

    @Query("""
         SELECT s FROM SessionEntity s
        WHERE s.account.id = :accountId
         AND s.revokedAt IS NULL
    """)
    List<SessionEntity> findAllByAccountIdAndNotRevoked(
            @Param("accountId") UUID accountId
    );

    @Query("""
        SELECT s FROM SessionEntity s
        WHERE s.account.id = :accountId
            AND s.id IN (:sessionsIds)
            AND s.revokedAt IS NULL
    """)
    List<SessionEntity> findAllByAccountIdAndSessionIdsAndNotRevoked(
            @Param("accountId") UUID accountId,
            @Param("sessionsIds") List<UUID> sessionsIds
    );

    @Modifying
    @Query("""
        DELETE FROM SessionEntity s
        WHERE s.revokedAt IS NOT NULL
            OR s.expiresAt < :now
    """)
    int deleteAllRevokedOrExpired(@Param("now") Instant now);
}
