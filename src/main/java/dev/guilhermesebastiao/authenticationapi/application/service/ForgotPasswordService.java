package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.ForgotPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ForgotPasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendForgotUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.*;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountDisabledException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ForgotPasswordService implements ForgotPasswordUseCase {
    private static final int RESEND_EMAIL_AVAILABLE_MINUTES = 1;
    private static final int FORGOT_EXPIRES_MINUTES = 15;
    private static final int FORGOT_TOKEN_SIZE = 32;

    private final ForgotPasswordRepositoryPort forgotPasswordRepository;
    private final SecureRandomGeneratorPort secureRandomGenerator;
    private final AccountRepositoryPort accountRepository;
    private final SendForgotUseCase sendForgotUseCase;
    private final PasswordEncoderPort passwordEncoder;
    private final SecureHasherPort secureHasher;
    private final SecureRandom secureRandom;

    public ForgotPasswordService(
            ForgotPasswordRepositoryPort forgotPasswordRepository,
            SecureRandomGeneratorPort secureRandomGenerator,
            AccountRepositoryPort accountRepository,
            SendForgotUseCase sendForgotUseCase,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.secureRandomGenerator = secureRandomGenerator;
        this.sendForgotUseCase = sendForgotUseCase;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureRandom = secureRandom;
        this.secureHasher = secureHasher;
    }

    @Override
    public ForgotPasswordOutput execute(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(AccountNotFoundException::new);

        if (account.getDisabledAt() != null) {
            throw new AccountDisabledException();
        }

        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));

        String forgotToken = secureRandomGenerator.generate(FORGOT_TOKEN_SIZE);

        ForgotPassword forgotPassword = createForgotPassword(account, forgotToken, otp);

        sendForgotUseCase.execute(email, otp, forgotPassword.getExpiresAt());

        forgotPasswordRepository.save(forgotPassword);

        return new ForgotPasswordOutput(
                forgotPassword.getAccount().getEmail(),
                forgotToken,
                forgotPassword.getResendAvailableAt(),
                forgotPassword.getExpiresAt()
        );
    }

    private ForgotPassword createForgotPassword(Account account, String forgotToken, String otp) {
        Instant resendAvailableAt = Instant.now().plus(RESEND_EMAIL_AVAILABLE_MINUTES, ChronoUnit.MINUTES);
        Instant expiresAt = Instant.now().plus(FORGOT_EXPIRES_MINUTES, ChronoUnit.MINUTES);

        String recoverTokenHash = secureHasher.hash(forgotToken);
        String otpHash = passwordEncoder.hash(otp);

        ForgotPassword entity = new ForgotPassword();

        entity.setAccount(account);
        entity.setForgotTokenHash(recoverTokenHash);
        entity.setOtpHash(otpHash);
        entity.setExpiresAt(expiresAt);
        entity.setResendAvailableAt(resendAvailableAt);

        return entity;
    }
}
