package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.command.ValidateRecoverPasswordCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ValidateRecoverPasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.result.RecoverPasswordResult;
import br.com.guisebastiao.authenticationapi.domain.exception.*;
import dev.guilhermesebastiao.authenticationapi.domain.exception.IncorrectOtpException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordAlreadyUsedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoverPassword;

import java.time.Instant;

public class ValidateRecoverPasswordService implements ValidateRecoverPasswordUseCase {
    private final RecoverPasswordRepositoryPort recoverPasswordRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;

    public ValidateRecoverPasswordService(
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher

    ) {
        this.recoverPasswordRepository = recoverPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher =secureHasher;
    }

    @Override
    public RecoverPasswordResult execute(ValidateRecoverPasswordCommand command) {
        String recoverTokenHash = secureHasher.hash(command.recoverToken());

        RecoverPassword recoverPassword = recoverPasswordRepository
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash)
                .orElseThrow(RecoverPasswordNotFoundException::new);

        validateRecoverPassword(recoverPassword);

        boolean otpIsValid = passwordEncoder.matches(command.otpCode(), recoverPassword.getOtpCodeHash());

        if (!otpIsValid) {
            throw new IncorrectOtpException();
        }

        recoverPassword.setVerifiedAt(Instant.now());

        RecoverPassword saved = recoverPasswordRepository.save(recoverPassword);

        boolean isVerified = saved.getVerifiedAt() != null;

        return new RecoverPasswordResult(
                saved.getAccount().getEmail(),
                command.recoverToken(),
                saved.getResendAvailableAt(),
                saved.getExpiresAt(),
                isVerified
        );
    }

    private void validateRecoverPassword(RecoverPassword recoverPassword) {
        if (!recoverPassword.getExpiresAt().isAfter(Instant.now())) {
            throw new RecoverPasswordExpiredException();
        }

        if (recoverPassword.getUsedAt() != null) {
            throw new RecoverPasswordAlreadyUsedException();
        }
    }
}
