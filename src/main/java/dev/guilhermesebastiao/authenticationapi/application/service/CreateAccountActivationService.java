package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CreateAccountActivationService implements CreateAccountActivationUseCase {
    private static final int ACCOUNT_ACTIVATION_EXPIRES_MINUTES = 15;
    private static final int RESEND_EMAIL_AVAILABLE_MINUTES = 1;
    private static final int ACTIVATION_TOKEN_SIZE = 32;

    private final SendAccountActivationUseCase sendAccountActivationUseCase;
    private final AccountActivationRepositoryPort accountActivationRepository;
    private final SecureRandomGeneratorPort secureRandomGenerator;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;
    private final SecureRandom secureRandom;

    public CreateAccountActivationService(
            SendAccountActivationUseCase sendAccountActivationUseCase,
            AccountActivationRepositoryPort accountActivationRepository,
            SecureRandomGeneratorPort secureRandomGenerator,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        this.sendAccountActivationUseCase = sendAccountActivationUseCase;
        this.accountActivationRepository = accountActivationRepository;
        this.secureRandomGenerator = secureRandomGenerator;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher = secureHasher;
        this.secureRandom = secureRandom;
    }

    @Override
    public AccountActivationOutput execute(Account account) {
        if (account.getStatus() == AccountStatus.ACTIVATED) {
            throw new AccountAlreadyActivatedException();
        }

        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        String otpHash = passwordEncoder.hash(otp);

        Instant expiresAt = Instant.now().plus(ACCOUNT_ACTIVATION_EXPIRES_MINUTES, ChronoUnit.MINUTES);
        Instant resendAvailableAt = Instant.now().plus(RESEND_EMAIL_AVAILABLE_MINUTES, ChronoUnit.MINUTES);

        String activationToken = secureRandomGenerator.generate(ACTIVATION_TOKEN_SIZE);

        String activationTokenHash = secureHasher.hash(activationToken);

        AccountActivation accountActivationEntity = new AccountActivation();
        accountActivationEntity.setAccount(account);
        accountActivationEntity.setActivationTokenHash(activationTokenHash);
        accountActivationEntity.setOtpHash(otpHash);
        accountActivationEntity.setExpiresAt(expiresAt);
        accountActivationEntity.setResendAvailableAt(resendAvailableAt);

        accountActivationRepository.save(accountActivationEntity);

        sendAccountActivationUseCase.execute(account.getEmail(), otp, expiresAt);

        return new AccountActivationOutput(account.getEmail(), activationToken, expiresAt, resendAvailableAt);
    }
}
