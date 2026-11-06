package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.ResendForgotPasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendForgotUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.ForgotPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.ForgotPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.domain.exception.*;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ResendForgotPasswordService implements ResendForgotPasswordUseCase {
    private static final int RESEND_EMAIL_AVAILABLE_MINUTES = 1;

    private final ForgotPasswordRepositoryPort forgotPasswordRepository;
    private final SendForgotUseCase sendForgotUseCase;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;
    private final SecureRandom secureRandom;

    public ResendForgotPasswordService(
            ForgotPasswordRepositoryPort forgotPasswordRepository,
            SendForgotUseCase sendForgotUseCase,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.sendForgotUseCase = sendForgotUseCase;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher = secureHasher;
        this.secureRandom = secureRandom;
    }

    @Override
    public ForgotPasswordOutput execute(String forgotToken) {
        String forgotTokenHash = secureHasher.hash(forgotToken);

        ForgotPassword forgotPassword = forgotPasswordRepository
                .findByForgotTokenHash(forgotTokenHash)
                .orElseThrow(ForgotPasswordNotFoundException::new);

        validateForgotPassword(forgotPassword);

        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        String otpHash = passwordEncoder.hash(otp);

        Instant resendAvailableAt = Instant.now().plus(RESEND_EMAIL_AVAILABLE_MINUTES, ChronoUnit.MINUTES);

        forgotPassword.setResendAvailableAt(resendAvailableAt);
        forgotPassword.setOtpHash(otpHash);

        forgotPasswordRepository.save(forgotPassword);

        String email = forgotPassword.getAccount().getEmail();

        sendForgotUseCase.execute(
                email,
                otp,
                forgotPassword.getExpiresAt()
        );

        return new ForgotPasswordOutput(
                email,
                forgotToken,
                resendAvailableAt,
                forgotPassword.getExpiresAt()
        );
    }

    private void validateForgotPassword(ForgotPassword forgotPassword) {
        if (forgotPassword.getVerifiedAt() != null) {
            throw new ForgotPasswordAlreadyVerifiedException();
        }

        if (!forgotPassword.getExpiresAt().isAfter(Instant.now())) {
            throw new ForgotPasswordExpiredException();
        }

        if (forgotPassword.getResendAvailableAt().isAfter(Instant.now())) {
            throw new ForgotPasswordResendNotAvailableException();
        }
    }
}
