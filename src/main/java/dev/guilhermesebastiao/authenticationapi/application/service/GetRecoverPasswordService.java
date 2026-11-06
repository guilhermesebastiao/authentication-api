package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.GetRecoverPasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.result.RecoverPasswordResult;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordAlreadyUsedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoverPassword;

import java.time.Instant;

public class GetRecoverPasswordService implements GetRecoverPasswordUseCase {
    private final RecoverPasswordRepositoryPort recoverPasswordRepository;
    private final SecureHasherPort secureHasher;

    public GetRecoverPasswordService(
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            SecureHasherPort secureHasher
    ) {
        this.recoverPasswordRepository = recoverPasswordRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public RecoverPasswordResult execute(String recoverToken) {
        String refreshTokenHash = secureHasher.hash(recoverToken);

        RecoverPassword recoverPassword = recoverPasswordRepository
                .findByRecoverTokenHash(refreshTokenHash)
                .orElseThrow(RecoverPasswordNotFoundException::new);

        validateRecoverPassword(recoverPassword, Instant.now());

        boolean isVerified = recoverPassword.getVerifiedAt() != null;

        return new RecoverPasswordResult(
                recoverPassword.getAccount().getEmail(),
                recoverToken,
                recoverPassword.getResendAvailableAt(),
                recoverPassword.getExpiresAt(),
                isVerified
        );
    }

    private void validateRecoverPassword(RecoverPassword recoverPassword, Instant now) {
        if (!recoverPassword.getExpiresAt().isAfter(now)) {
            throw new RecoverPasswordExpiredException();
        }

        if (recoverPassword.getUsedAt() != null) {
            throw new RecoverPasswordAlreadyUsedException();
        }
    }
}
