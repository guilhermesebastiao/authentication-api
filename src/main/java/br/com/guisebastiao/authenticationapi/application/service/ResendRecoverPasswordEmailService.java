package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.port.in.ResendRecoverPasswordEmailUseCase;
import br.com.guisebastiao.authenticationapi.application.port.in.SendRecoverPasswordEmailUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import br.com.guisebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.SecureHasherPort;
import br.com.guisebastiao.authenticationapi.application.result.RecoverPasswordResult;
import br.com.guisebastiao.authenticationapi.domain.exception.*;
import br.com.guisebastiao.authenticationapi.domain.model.RecoverPassword;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ResendRecoverPasswordEmailService implements ResendRecoverPasswordEmailUseCase {
    private static final int RESEND_EMAIL_AVAILABLE_MINUTES = 1;

    private final SendRecoverPasswordEmailUseCase sendRecoverPasswordEmailUseCase;
    private final RecoverPasswordRepositoryPort recoverPasswordRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;
    private final SecureRandom secureRandom;

    public ResendRecoverPasswordEmailService(
            SendRecoverPasswordEmailUseCase sendRecoverPasswordEmailUseCase,
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        this.sendRecoverPasswordEmailUseCase = sendRecoverPasswordEmailUseCase;
        this.recoverPasswordRepository = recoverPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher = secureHasher;
        this.secureRandom = secureRandom;
    }

    @Override
    public RecoverPasswordResult execute(String recoverToken) {
        String recoverTokenHash = secureHasher.hash(recoverToken);

        RecoverPassword recoverPassword = recoverPasswordRepository
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash)
                .orElseThrow(RecoverPasswordNotFoundException::new);

        validateRecoverPassword(recoverPassword);

        String otpCode = String.format("%06d", secureRandom.nextInt(1_000_000));
        String otpCodeHash = passwordEncoder.hash(otpCode);

        Instant resendAvailableAt = Instant.now().plus(RESEND_EMAIL_AVAILABLE_MINUTES, ChronoUnit.MINUTES);

        recoverPassword.setResendAvailableAt(resendAvailableAt);
        recoverPassword.setOtpCodeHash(otpCodeHash);

        RecoverPassword saved = recoverPasswordRepository.save(recoverPassword);

        String email = saved.getAccount().getEmail();

        sendRecoverPasswordEmailUseCase.execute(
                email,
                otpCode,
                saved.getExpiresAt()
        );

        return new RecoverPasswordResult(
                email,
                recoverToken,
                resendAvailableAt,
                saved.getExpiresAt(),
                false
        );
    }

    private void validateRecoverPassword(RecoverPassword recoverPassword) {
        if (recoverPassword.getVerifiedAt() != null) {
            throw new RecoverPasswordAlreadyVerifiedException();
        }

        if (!recoverPassword.getExpiresAt().isAfter(Instant.now())) {
            throw new RecoverPasswordExpiredException();
        }

        if (recoverPassword.getResendAvailableAt().isAfter(Instant.now())) {
            throw new RecoverPasswordResendNotAvailableException();
        }
    }
}
