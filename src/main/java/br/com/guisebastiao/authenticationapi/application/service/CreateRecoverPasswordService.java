package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.command.CreateRecoverPasswordCommand;
import br.com.guisebastiao.authenticationapi.application.port.in.CreateRecoverPasswordUseCase;
import br.com.guisebastiao.authenticationapi.application.port.in.SendRecoverPasswordEmailUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.*;
import br.com.guisebastiao.authenticationapi.application.result.RecoverPasswordResult;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountNotFoundException;
import br.com.guisebastiao.authenticationapi.domain.model.Account;
import br.com.guisebastiao.authenticationapi.domain.model.RecoverPassword;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CreateRecoverPasswordService implements CreateRecoverPasswordUseCase {
    private static final int RECOVER_PASSWORD_EXPIRES_MINUTES = 15;
    private static final int RESEND_EMAIL_AVAILABLE_MINUTES = 1;
    private static final int RECOVER_TOKEN_SIZE = 32;

    private final SendRecoverPasswordEmailUseCase sendRecoverPasswordEmailUseCase;
    private final RecoverPasswordRepositoryPort recoverPasswordRepository;
    private final SecureRandomGeneratorPort secureRandomGenerator;
    private final AccountRepositoryPort accountRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;
    private final SecureRandom secureRandom;

    public CreateRecoverPasswordService(
            SendRecoverPasswordEmailUseCase sendRecoverPasswordEmailUseCase,
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            SecureRandomGeneratorPort secureRandomGenerator,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        this.sendRecoverPasswordEmailUseCase = sendRecoverPasswordEmailUseCase;
        this.recoverPasswordRepository = recoverPasswordRepository;
        this.secureRandomGenerator = secureRandomGenerator;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureRandom = secureRandom;
        this.secureHasher = secureHasher;
    }

    @Override
    public RecoverPasswordResult execute(CreateRecoverPasswordCommand command) {
        String otpCode = String.format("%06d", secureRandom.nextInt(1_000_000));

        String recoverToken = secureRandomGenerator.generate(RECOVER_TOKEN_SIZE);

        Account account = accountRepository.findByEmail(command.email())
                .orElseThrow(AccountNotFoundException::new);

        RecoverPassword recoverPassword = createRecoverPassword(account, recoverToken, otpCode);

        sendRecoverPasswordEmailUseCase.execute(command.email(), otpCode, recoverPassword.getExpiresAt());

        recoverPasswordRepository.save(recoverPassword);

        return new RecoverPasswordResult(
                recoverPassword.getAccount().getEmail(),
                recoverToken,
                recoverPassword.getResendAvailableAt(),
                recoverPassword.getExpiresAt(),
                false
        );
    }

    private RecoverPassword createRecoverPassword(Account account, String recoverToken, String otpCode) {
        Instant expiresAt = Instant.now().plus(RECOVER_PASSWORD_EXPIRES_MINUTES, ChronoUnit.MINUTES);
        Instant resendAvailableAt = Instant.now().plus(RESEND_EMAIL_AVAILABLE_MINUTES, ChronoUnit.MINUTES);

        String recoverTokenHash = secureHasher.hash(recoverToken);
        String otpCodeHash = passwordEncoder.hash(otpCode);

        RecoverPassword recoverPasswordEntity = new RecoverPassword();

        recoverPasswordEntity.setAccount(account);
        recoverPasswordEntity.setRecoverTokenHash(recoverTokenHash);
        recoverPasswordEntity.setOtpCodeHash(otpCodeHash);
        recoverPasswordEntity.setExpiresAt(expiresAt);
        recoverPasswordEntity.setResendAvailableAt(resendAvailableAt);

        return recoverPasswordEntity;
    }
}
