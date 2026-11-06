package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.AccountActivationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAccountActivationRepository extends JpaRepository<AccountActivationEntity, UUID> {
    Optional<AccountActivationEntity> findByActivationTokenHash(String activationToken);

    @Modifying
    @Query("""
        DELETE FROM AccountActivationEntity aa
        WHERE aa.expiresAt < :now
    """)
    int deleteAllExpired(@Param("now") Instant now);
}
