package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.DisableAccountInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.DisableAccountUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

import java.time.Instant;

public class DisableAccountService implements DisableAccountUseCase {
    private final AccountRepositoryPort accountRepository;
    private final RevokeAllSessionsUseCase revokeAllSessionsUseCase;
    private final PasswordEncoderPort passwordEncoder;

    public DisableAccountService(
            AccountRepositoryPort accountRepository,
            RevokeAllSessionsUseCase revokeAllSessionsUseCase,
            PasswordEncoderPort passwordEncoder
    ) {
        this.accountRepository = accountRepository;
        this.revokeAllSessionsUseCase = revokeAllSessionsUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(Account account, DisableAccountInput input) {
        boolean isMatch = passwordEncoder.matches(input.password(), account.getPasswordHash());

        if (!isMatch) {
            throw new AccountInvalidCredentialsException();
        }

        account.setDisabledAt(Instant.now());

        accountRepository.save(account);

        revokeAllSessionsUseCase.execute(account);
    }
}
