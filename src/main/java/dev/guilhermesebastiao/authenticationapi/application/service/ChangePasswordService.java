package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.ChangePasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ChangePasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendPasswordChangedUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ChangePasswordInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.PasswordSameAsCurrentException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public class ChangePasswordService implements ChangePasswordUseCase {
    private final SendPasswordChangedUseCase sendPasswordChangedUseCase;
    private final RevokeAllSessionsUseCase revokeAllSessionsUseCase;
    private final AccountRepositoryPort accountRepository;
    private final PasswordEncoderPort passwordEncoder;

    public ChangePasswordService(
            SendPasswordChangedUseCase sendPasswordChangedUseCase,
            RevokeAllSessionsUseCase revokeAllSessionsUseCase,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder
    ) {
        this.sendPasswordChangedUseCase = sendPasswordChangedUseCase;
        this.revokeAllSessionsUseCase = revokeAllSessionsUseCase;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(Account account, ChangePasswordInput input) {
        boolean isMatch = passwordEncoder.matches(input.currentPassword(), account.getPasswordHash());

        if (!isMatch) {
            throw new ChangePasswordInvalidCredentialsException();
        }

        boolean samePassword = passwordEncoder.matches(input.newPassword(), account.getPasswordHash());

        if (samePassword) {
            throw new PasswordSameAsCurrentException();
        }

        String newPasswordHash = passwordEncoder.hash(input.newPassword());

        account.setPasswordHash(newPasswordHash);

        accountRepository.save(account);

        revokeAllSessionsUseCase.execute(account);

        sendPasswordChangedUseCase.execute(account.getEmail());
    }
}
