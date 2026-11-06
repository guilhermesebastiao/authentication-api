package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.RecoveryPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetRecoveryPasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoveryPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoveryPasswordAlreadyUsedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ForgotPasswordExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoveryPasswordNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;

import java.time.Instant;

public class GetRecoveryPasswordService implements GetRecoveryPasswordUseCase {
    private final RecoveryPasswordRepositoryPort recoveryPasswordRepository;
    private final SecureHasherPort secureHasher;

    public GetRecoveryPasswordService(
            RecoveryPasswordRepositoryPort recoveryPasswordRepository,
            SecureHasherPort secureHasher
    ) {
        this.recoveryPasswordRepository = recoveryPasswordRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public RecoveryPasswordOutput execute(String recoveryToken) {
        String recoveryTokenHash = secureHasher.hash(recoveryToken);

        RecoveryPassword recoveryPassword = recoveryPasswordRepository
                .findByRecoveryTokenHash(recoveryTokenHash)
                .orElseThrow(RecoveryPasswordNotFoundException::new);

        validateRecoverPassword(recoveryPassword, Instant.now());

        boolean isUsed = recoveryPassword.getUsedAt() != null;

        return new RecoveryPasswordOutput(
                recoveryToken,
                recoveryPassword.getExpiresAt(),
                isUsed
        );
    }

    private void validateRecoverPassword(RecoveryPassword recoveryPassword, Instant now) {
        if (!recoveryPassword.getExpiresAt().isAfter(now)) {
            throw new ForgotPasswordExpiredException();
        }

        if (recoveryPassword.getUsedAt() != null) {
            throw new RecoveryPasswordAlreadyUsedException();
        }
    }
}
