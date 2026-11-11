package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.AccountActivationInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.AccountActivateUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountWelcomeUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.IncorrectOtpException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;

import java.time.Instant;

public class AccountActivateService implements AccountActivateUseCase {
    private final AccountActivationRepositoryPort accountActivationRepository;
    private final SendAccountWelcomeUseCase sendAccountWelcomeUseCase;
    private final AccountRepositoryPort accountRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;

    public AccountActivateService(
            AccountActivationRepositoryPort accountActivationRepository,
            SendAccountWelcomeUseCase sendAccountWelcomeUseCase,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher
    ) {
        this.accountActivationRepository = accountActivationRepository;
        this.sendAccountWelcomeUseCase = sendAccountWelcomeUseCase;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher = secureHasher;
    }

    @Override
    public void execute(AccountActivationInput input) {
        String activationTokenHash = secureHasher.hash(input.activationToken());

        AccountActivation accountActivation = accountActivationRepository
                .findByActivationTokenHash(activationTokenHash)
                .orElseThrow(AccountActivationNotFoundException::new);

        validateAccountActivation(accountActivation);

        boolean otpIsValid = passwordEncoder.matches(input.otp(), accountActivation.getOtpHash());

        if (!otpIsValid) {
            throw new IncorrectOtpException();
        }

        activateAccount(accountActivation);

        sendAccountWelcomeUseCase.execute(accountActivation.getAccount().getEmail());
    }

    private void activateAccount(AccountActivation accountActivation) {
        Account account = accountActivation.getAccount();
        account.setStatus(AccountStatus.ACTIVATED);

        accountActivation.setActivatedAt(Instant.now());

        accountRepository.save(account);
        accountActivationRepository.save(accountActivation);
    }

    private void validateAccountActivation(AccountActivation accountActivation) {
        if (accountActivation.getActivatedAt() != null) {
            throw new AccountAlreadyActivatedException();
        }

        if (!accountActivation.getExpiresAt().isAfter(Instant.now())) {
            throw new AccountActivationExpiredException();
        }
    }
}
