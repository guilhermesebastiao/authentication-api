package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.ValidateForgotPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.RecoveryPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ValidateForgotPasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.*;
import dev.guilhermesebastiao.authenticationapi.domain.exception.*;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ValidateForgotPasswordService implements ValidateForgotPasswordUseCase {
    private static final int RECOVERY_EXPIRES_MINUTES = 5;
    private static final int RECOVERY_TOKEN_SIZE = 32;

    private final RecoveryPasswordRepositoryPort recoveryPasswordRepository;
    private final ForgotPasswordRepositoryPort forgotPasswordRepository;
    private final SecureRandomGeneratorPort secureRandomGenerator;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;

    public ValidateForgotPasswordService(
            RecoveryPasswordRepositoryPort recoveryPasswordRepository,
            ForgotPasswordRepositoryPort forgotPasswordRepository,
            SecureRandomGeneratorPort secureRandomGenerator,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher

    ) {
        this.recoveryPasswordRepository = recoveryPasswordRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.secureRandomGenerator = secureRandomGenerator;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher =secureHasher;
    }

    @Override
    public RecoveryPasswordOutput execute(ValidateForgotPasswordInput input) {
        String forgotTokenHash = secureHasher.hash(input.forgotToken());

        ForgotPassword forgotPassword = forgotPasswordRepository
                .findByForgotTokenHash(forgotTokenHash)
                .orElseThrow(ForgotPasswordNotFoundException::new);

        validateForgotPassword(forgotPassword);

        boolean otpIsValid = passwordEncoder.matches(input.otp(), forgotPassword.getOtpHash());

        if (!otpIsValid) {
            throw new IncorrectOtpException();
        }

        forgotPassword.setVerifiedAt(Instant.now());

        ForgotPassword forgotPasswordSaved = forgotPasswordRepository.save(forgotPassword);

        String recoveryToken = secureRandomGenerator.generate(RECOVERY_TOKEN_SIZE);

        RecoveryPassword recoveryPassword = createRecoveryPassword(forgotPasswordSaved, recoveryToken);

        recoveryPasswordRepository.save(recoveryPassword);

        return new RecoveryPasswordOutput(
                recoveryToken,
                recoveryPassword.getExpiresAt(),
                false
        );
    }

    private void validateForgotPassword(ForgotPassword forgotPassword) {
        if (forgotPassword.getVerifiedAt() != null) {
            throw new ForgotPasswordAlreadyVerifiedException();
        }

        if (!forgotPassword.getExpiresAt().isAfter(Instant.now())) {
            throw new ForgotPasswordExpiredException();
        }
    }

    private RecoveryPassword createRecoveryPassword(
            ForgotPassword forgotPassword,
            String recoveryToken
    ) {
        RecoveryPassword entity = new RecoveryPassword();

        Instant expiresAt = Instant.now().plus(RECOVERY_EXPIRES_MINUTES, ChronoUnit.MINUTES);

        String recoveryTokenHash = secureHasher.hash(recoveryToken);

        entity.setRecoveryTokenHash(recoveryTokenHash);
        entity.setExpiresAt(expiresAt);
        entity.setForgotPassword(forgotPassword);

        return entity;
    }
}
