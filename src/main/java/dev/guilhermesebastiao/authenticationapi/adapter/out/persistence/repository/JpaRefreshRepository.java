package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRefreshRepository extends JpaRepository<RefreshEntity, UUID> {
    Optional<RefreshEntity> findByRefreshTokenHash(String refreshTokenHash);

    @Query("""
        SELECT r FROM RefreshEntity r
            WHERE r.session.id IN (:sessionId)
                AND r.revokedAt IS NULL
    """)
    Optional<RefreshEntity> findByRefreshBySessionId(@Param("sessionId") UUID sessionId);

    @Query("""
        SELECT r FROM RefreshEntity r
            WHERE r.session.id IN (:sessionIds)
              AND r.revokedAt IS NULL
    """)
    List<RefreshEntity> findAllBySessionIdsAndNotRevoked(@Param("sessionIds") List<UUID> sessionIds);
}
