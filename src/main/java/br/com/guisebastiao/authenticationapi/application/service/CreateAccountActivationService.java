package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.port.in.CreateAccountActivationUseCase;
import br.com.guisebastiao.authenticationapi.application.port.in.SendAccountActivationEmailUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.*;
import br.com.guisebastiao.authenticationapi.application.result.AccountActivationResult;
import br.com.guisebastiao.authenticationapi.domain.enums.AccountStatus;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import br.com.guisebastiao.authenticationapi.domain.model.Account;
import br.com.guisebastiao.authenticationapi.domain.model.AccountActivation;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CreateAccountActivationService implements CreateAccountActivationUseCase {
    private static final int ACCOUNT_ACTIVATION_EXPIRES_MINUTES = 15;
    private static final int RESEND_EMAIL_AVAILABLE_MINUTES = 1;
    private static final int ACTIVATION_TOKEN_SIZE = 32;

    private final SendAccountActivationEmailUseCase sendAccountActivationEmailUseCase;
    private final AccountActivationRepositoryPort accountActivationRepository;
    private final SecureRandomGeneratorPort secureRandomGenerator;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;
    private final SecureRandom secureRandom;

    public CreateAccountActivationService(
            SendAccountActivationEmailUseCase sendAccountActivationEmailUseCase,
            AccountActivationRepositoryPort accountActivationRepository,
            SecureRandomGeneratorPort secureRandomGenerator,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        this.sendAccountActivationEmailUseCase = sendAccountActivationEmailUseCase;
        this.accountActivationRepository = accountActivationRepository;
        this.secureRandomGenerator = secureRandomGenerator;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher = secureHasher;
        this.secureRandom = secureRandom;
    }

    @Override
    public AccountActivationResult execute(Account account) {
        if (account.getStatus() == AccountStatus.ACTIVATED) {
            throw new AccountAlreadyActivatedException();
        }

        String otpCode = String.format("%06d", secureRandom.nextInt(1_000_000));
        String otpCodeHash = passwordEncoder.hash(otpCode);

        Instant expiresAt = Instant.now().plus(ACCOUNT_ACTIVATION_EXPIRES_MINUTES, ChronoUnit.MINUTES);
        Instant resendAvailableAt = Instant.now().plus(RESEND_EMAIL_AVAILABLE_MINUTES, ChronoUnit.MINUTES);

        String activationToken = secureRandomGenerator.generate(ACTIVATION_TOKEN_SIZE);

        String activationTokenHash = secureHasher.hash(activationToken);

        AccountActivation accountActivationEntity = new AccountActivation();
        accountActivationEntity.setAccount(account);
        accountActivationEntity.setActivationTokenHash(activationTokenHash);
        accountActivationEntity.setOtpCodeHash(otpCodeHash);
        accountActivationEntity.setExpiresAt(expiresAt);
        accountActivationEntity.setResendAvailableAt(resendAvailableAt);

        accountActivationRepository.save(accountActivationEntity);

        sendAccountActivationEmailUseCase.execute(account.getEmail(), otpCode, expiresAt);

        return new AccountActivationResult(account.getEmail(), activationToken, expiresAt, resendAvailableAt);
    }
}
