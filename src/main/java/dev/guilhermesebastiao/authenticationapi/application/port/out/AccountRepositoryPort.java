package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {
    Account save(Account account);
    Optional<Account> findById(UUID id);
    Optional<Account> findByEmail(String email);
}
