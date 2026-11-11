package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoveryPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRecoveryPasswordRepository extends JpaRepository<RecoveryPasswordEntity, UUID> {
    Optional<RecoveryPasswordEntity> findByRecoveryTokenHash(String recoverTokenHash);

    @Modifying
    @Query("""
        DELETE FROM RecoveryPasswordEntity rp
            WHERE rp.expiresAt <= :now
    """)
    int deleteAllExpiredOrUsed(@Param("now")Instant now);
}
