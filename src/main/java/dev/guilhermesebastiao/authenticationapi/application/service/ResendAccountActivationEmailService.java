package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.ResendAccountActivationEmailUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountActivationEmailUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.result.AccountActivationResult;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationResendNotAvailableException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ResendAccountActivationEmailService implements ResendAccountActivationEmailUseCase {
    private static final int RESEND_EMAIL_AVAILABLE_MINUTES = 1;

    private final SendAccountActivationEmailUseCase sendAccountActivationEmailUseCase;
    private final AccountActivationRepositoryPort accountActivationRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;
    private final SecureRandom secureRandom;

    public ResendAccountActivationEmailService(
            SendAccountActivationEmailUseCase sendAccountActivationEmailUseCase,
            AccountActivationRepositoryPort accountActivationRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        this.sendAccountActivationEmailUseCase = sendAccountActivationEmailUseCase;
        this.accountActivationRepository = accountActivationRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher = secureHasher;
        this.secureRandom = secureRandom;
    }

    @Override
    public AccountActivationResult execute(String activationToken) {
        Instant now = Instant.now();

        String activationTokenHash = secureHasher.hash(activationToken);

        AccountActivation accountActivationExists = accountActivationRepository
                .findByActivationTokenHash(activationTokenHash)
                .orElseThrow(AccountActivationNotFoundException::new);

        validateAccountActivation(accountActivationExists, now);

        String otpCode = String.format("%06d", secureRandom.nextInt(1_000_000));
        String otpCodeHash = passwordEncoder.hash(otpCode);

        Instant resendAvailableAt = now.plus(RESEND_EMAIL_AVAILABLE_MINUTES, ChronoUnit.MINUTES);

        accountActivationExists.setResendAvailableAt(resendAvailableAt);
        accountActivationExists.setOtpCodeHash(otpCodeHash);

        AccountActivation accountActivation = accountActivationRepository.save(accountActivationExists);

        String email = accountActivation.getAccount().getEmail();

        sendAccountActivationEmailUseCase.execute(
                email,
                otpCode,
                accountActivation.getExpiresAt()
        );

        return new AccountActivationResult(
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
