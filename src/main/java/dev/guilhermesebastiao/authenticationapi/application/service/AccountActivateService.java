package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.command.AccountActivationCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.in.AccountActivateUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import br.com.guisebastiao.authenticationapi.domain.exception.*;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.IncorrectOtpException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;

import java.time.Instant;

public class AccountActivateService implements AccountActivateUseCase {
    private final AccountActivationRepositoryPort accountActivationRepository;
    private final AccountRepositoryPort accountRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;

    public AccountActivateService(
            AccountActivationRepositoryPort accountActivationRepository,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher
    ) {
        this.accountActivationRepository = accountActivationRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher = secureHasher;
    }

    @Override
    public void execute(AccountActivationCommand command) {
        String activationTokenHash = secureHasher.hash(command.activationToken());

        AccountActivation accountActivation = accountActivationRepository
                .findByActivationTokenHash(activationTokenHash)
                .orElseThrow(AccountActivationNotFoundException::new);

        validateAccountActivation(accountActivation);

        boolean otpIsValid = passwordEncoder.matches(command.otpCode(), accountActivation.getOtpCodeHash());

        if (!otpIsValid) {
            throw new IncorrectOtpException();
        }

        activateAccount(accountActivation);
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
