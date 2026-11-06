package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.command.ChangePasswordCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ChangePasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SignOutAllUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.PasswordSameAsCurrentException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public class ChangePasswordService implements ChangePasswordUseCase {
    private final AccountRepositoryPort accountRepository;
    private final SignOutAllUseCase signOutAllUseCase;
    private final PasswordEncoderPort passwordEncoder;

    public ChangePasswordService(
            AccountRepositoryPort accountRepository,
            SignOutAllUseCase signOutAllUseCase,
            PasswordEncoderPort passwordEncoder
    ) {
        this.accountRepository = accountRepository;
        this.signOutAllUseCase = signOutAllUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(Account account, ChangePasswordCommand command) {
        boolean isMatch = passwordEncoder.matches(command.currentPassword(), account.getPasswordHash());

        if (!isMatch) {
            throw new AccountInvalidCredentialsException();
        }

        boolean samePassword = passwordEncoder.matches(command.newPassword(), account.getPasswordHash());

        if (samePassword) {
            throw new PasswordSameAsCurrentException();
        }

        String newPasswordHash = passwordEncoder.hash(command.newPassword());

        account.setPasswordHash(newPasswordHash);

        accountRepository.save(account);

        signOutAllUseCase.execute(account);
    }
}
