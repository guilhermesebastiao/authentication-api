package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.DisableAccountInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.DisableAccountUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountDisabledUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.DisableAccountInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

import java.time.Instant;

public class DisableAccountService implements DisableAccountUseCase {
    private final SendAccountDisabledUseCase sendAccountDisabledUseCase;
    private final RevokeAllSessionsUseCase revokeAllSessionsUseCase;
    private final AccountRepositoryPort accountRepository;
    private final PasswordEncoderPort passwordEncoder;

    public DisableAccountService(
            SendAccountDisabledUseCase sendAccountDisabledUseCase,
            RevokeAllSessionsUseCase revokeAllSessionsUseCase,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder
    ) {
        this.sendAccountDisabledUseCase = sendAccountDisabledUseCase;
        this.revokeAllSessionsUseCase = revokeAllSessionsUseCase;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(Account account, DisableAccountInput input) {
        boolean isMatch = passwordEncoder.matches(input.password(), account.getPasswordHash());

        if (!isMatch) {
            throw new DisableAccountInvalidCredentialsException();
        }

        account.setDisabledAt(Instant.now());

        accountRepository.save(account);

        revokeAllSessionsUseCase.execute(account);

        sendAccountDisabledUseCase.execute(account.getEmail());
    }
}
