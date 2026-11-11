package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;

import java.time.Instant;
import java.util.Optional;

public interface RecoveryPasswordRepositoryPort {
    RecoveryPassword save(RecoveryPassword recoveryPassword);

    Optional<RecoveryPassword> findByRecoveryTokenHash(String recoveryTokenHash);

    long deleteAllExpiredOrUsed(Instant now);
}
