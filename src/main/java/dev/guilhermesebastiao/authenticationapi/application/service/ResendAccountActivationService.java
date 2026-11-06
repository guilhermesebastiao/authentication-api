package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.ResendAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationResendNotAvailableException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ResendAccountActivationService implements ResendAccountActivationUseCase {
    private static final int RESEND_EMAIL_AVAILABLE_MINUTES = 1;

    private final SendAccountActivationUseCase sendAccountActivationUseCase;
    private final AccountActivationRepositoryPort accountActivationRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;
    private final SecureRandom secureRandom;

    public ResendAccountActivationService(
            SendAccountActivationUseCase sendAccountActivationUseCase,
            AccountActivationRepositoryPort accountActivationRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        this.sendAccountActivationUseCase = sendAccountActivationUseCase;
        this.accountActivationRepository = accountActivationRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher = secureHasher;
        this.secureRandom = secureRandom;
    }

    @Override
    public AccountActivationOutput execute(String activationToken) {
        Instant now = Instant.now();

        String activationTokenHash = secureHasher.hash(activationToken);

        AccountActivation accountActivationExists = accountActivationRepository
                .findByActivationTokenHash(activationTokenHash)
                .orElseThrow(AccountActivationNotFoundException::new);

        validateAccountActivation(accountActivationExists, now);

        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        String otpHash = passwordEncoder.hash(otp);

        Instant resendAvailableAt = now.plus(RESEND_EMAIL_AVAILABLE_MINUTES, ChronoUnit.MINUTES);

        accountActivationExists.setResendAvailableAt(resendAvailableAt);
        accountActivationExists.setOtpHash(otpHash);

        AccountActivation accountActivation = accountActivationRepository.save(accountActivationExists);

        String email = accountActivation.getAccount().getEmail();

        sendAccountActivationUseCase.execute(
                email,
                otp,
                accountActivation.getExpiresAt()
        );

        return new AccountActivationOutput(
                email,
                activationToken,
                accountActivation.getExpiresAt(),
                resendAvailableAt
        );
    }

    private void validateAccountActivation(AccountActivation accountActivation, Instant now) {
        if (!accountActivation.getExpiresAt().isAfter(now)) {
            throw new AccountActivationExpiredException();
        }

        if (accountActivation.getActivatedAt() != null) {
            throw new AccountAlreadyActivatedException();
        }

        if (accountActivation.getResendAvailableAt().isAfter(now)) {
            throw new AccountActivationResendNotAvailableException();
        }
    }
}
