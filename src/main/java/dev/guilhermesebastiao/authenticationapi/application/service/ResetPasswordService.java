package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.ResetPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ResetPasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoveryPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.*;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;

import java.time.Instant;

public class ResetPasswordService implements ResetPasswordUseCase {
    private final RecoveryPasswordRepositoryPort recoveryPasswordRepository;
    private final AccountRepositoryPort accountRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final RevokeAllSessionsUseCase revokeAllSessionsUseCase;
    private final SecureHasherPort secureHasher;

    public ResetPasswordService(
            RecoveryPasswordRepositoryPort recoveryPasswordRepository,
            AccountRepositoryPort accountRepository,
            RevokeAllSessionsUseCase revokeAllSessionsUseCase,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher
    ) {
        this.recoveryPasswordRepository = recoveryPasswordRepository;
        this.accountRepository = accountRepository;
        this.revokeAllSessionsUseCase = revokeAllSessionsUseCase;
        this.passwordEncoder = passwordEncoder;
        this.secureHasher = secureHasher;
    }

    @Override
    public void execute(ResetPasswordInput input) {
        Instant now = Instant.now();

        String recoveryTokenHash = secureHasher.hash(input.recoveryToken());

        RecoveryPassword recoveryPassword = recoveryPasswordRepository
                .findByRecoveryTokenHash(recoveryTokenHash)
                .orElseThrow(RecoveryPasswordNotFoundException::new);

        validateRecoverPassword(recoveryPassword, now);

        Account account = recoveryPassword.getForgotPassword().getAccount();

        account.setPasswordHash(passwordEncoder.hash(input.newPassword()));

        recoveryPassword.setUsedAt(now);

        accountRepository.save(account);

        revokeAllSessionsUseCase.execute(account);

        recoveryPasswordRepository.save(recoveryPassword);
    }

    private void validateRecoverPassword(RecoveryPassword recoveryPassword, Instant now) {
        if (!recoveryPassword.getExpiresAt().isAfter(now)) {
            throw new RecoveryPasswordExpiredException();
        }

        if (recoveryPassword.getUsedAt() != null) {
            throw new RecoveryPasswordAlreadyUsedException();
        }
    }
}
