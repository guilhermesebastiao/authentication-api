package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoverPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRecoverPasswordRepository extends JpaRepository<RecoverPasswordEntity, UUID> {
    Optional<RecoverPasswordEntity> findByRecoverTokenHash(String recoverToken);

    @Query("""
        SELECT rp FROM RecoverPasswordEntity rp
        WHERE rp.recoverTokenHash = :recoverTokenHash
            AND rp.verifiedAt IS NULL
    """)
    Optional<RecoverPasswordEntity> findByRecoverTokenHashAndNotVerified(
            @Param("recoverTokenHash") String recoverTokenHash
    );

    @Modifying
    @Query("""
        DELETE FROM RecoverPasswordEntity rp
        WHERE rp.expiresAt < :now
    """)
    int deleteAllExpired(@Param("now") Instant now);
}
