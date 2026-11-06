package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.ForgotPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaForgotPasswordRepository extends JpaRepository<ForgotPasswordEntity, UUID> {
    Optional<ForgotPasswordEntity> findByForgotTokenHash(String forgotTokenHash);
}
