package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoveryPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRecoveryPasswordRepository extends JpaRepository<RecoveryPasswordEntity, UUID> {
    Optional<RecoveryPasswordEntity> findByRecoveryTokenHash(String recoverTokenHash);
}
