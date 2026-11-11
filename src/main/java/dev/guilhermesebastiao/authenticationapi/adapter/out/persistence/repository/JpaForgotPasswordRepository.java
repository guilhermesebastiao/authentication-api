package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.ForgotPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaForgotPasswordRepository extends JpaRepository<ForgotPasswordEntity, UUID> {
    Optional<ForgotPasswordEntity> findByForgotTokenHash(String forgotTokenHash);

    @Modifying
    @Query("""
        DELETE FROM ForgotPasswordEntity fp
            WHERE COALESCE(
                (SELECT rp.expiresAt
                    FROM RecoveryPasswordEntity rp
                    WHERE rp.forgotPassword = fp),
                    fp.expiresAt
                 ) <= :now
    """)
    int deleteAllExpiredOrUsed(@Param("now") Instant now);
}
