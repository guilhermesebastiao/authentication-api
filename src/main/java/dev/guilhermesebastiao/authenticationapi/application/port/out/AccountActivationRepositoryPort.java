package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AccountActivationRepositoryPort {
    void save(List<AccountActivation> accountActivation);

    AccountActivation save(AccountActivation accountActivation);

    Optional<AccountActivation> findByActivationTokenHash(String activationTokenHash);

    long deleteAllExpiredOrUsed(Instant now);
}
