package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.port.in.GetRecoverPasswordUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.SecureHasherPort;
import br.com.guisebastiao.authenticationapi.application.result.RecoverPasswordResult;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordAlreadyUsedException;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordExpiredException;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordNotFoundException;
import br.com.guisebastiao.authenticationapi.domain.model.RecoverPassword;

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

        return new RecoverPasswordResult(
                recoverPassword.getAccount().getEmail(),
                recoverToken,
                recoverPassword.getResendAvailableAt(),
                recoverPassword.getExpiresAt()
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
