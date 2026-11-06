package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.command.DisableAccountCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.in.DisableAccountUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SignOutAllUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

import java.time.Instant;

public class DisableAccountService implements DisableAccountUseCase {
    private final AccountRepositoryPort accountRepository;
    private final SignOutAllUseCase signOutAllUseCase;
    private final PasswordEncoderPort passwordEncoder;

    public DisableAccountService(
            AccountRepositoryPort accountRepository,
            SignOutAllUseCase signOutAllUseCase,
            PasswordEncoderPort passwordEncoder
    ) {
        this.accountRepository = accountRepository;
        this.signOutAllUseCase = signOutAllUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(Account account, DisableAccountCommand command) {
        boolean isMatch = passwordEncoder.matches(command.password(), account.getPasswordHash());

        if (!isMatch) {
            throw new AccountInvalidCredentialsException();
        }

        account.setDisabledAt(Instant.now());

        accountRepository.save(account);

        signOutAllUseCase.execute(account);
    }
}
