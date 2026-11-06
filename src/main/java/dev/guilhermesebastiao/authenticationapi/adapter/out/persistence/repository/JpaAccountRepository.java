package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByEmail(String email);
}
