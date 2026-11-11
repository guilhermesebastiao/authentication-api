package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.ForgotPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetForgotPasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.ForgotPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ForgotPasswordAlreadyVerifiedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ForgotPasswordExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ForgotPasswordNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;

import java.time.Instant;

public class GetForgotPasswordService implements GetForgotPasswordUseCase {
    private final ForgotPasswordRepositoryPort forgotPasswordRepository;
    private final SecureHasherPort secureHasher;

    public GetForgotPasswordService(
            ForgotPasswordRepositoryPort forgotPasswordRepository,
            SecureHasherPort secureHasher
    ) {
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public ForgotPasswordOutput execute(String forgotToken) {
        String forgotTokenHash = secureHasher.hash(forgotToken);

        ForgotPassword forgotPassword = forgotPasswordRepository
                .findByForgotTokenHash(forgotTokenHash)
                .orElseThrow(ForgotPasswordNotFoundException::new);

        validateForgotPassword(forgotPassword);

        String email = forgotPassword.getAccount().getEmail();

        return new ForgotPasswordOutput(
                email,
                forgotToken,
                forgotPassword.getResendAvailableAt(),
                forgotPassword.getExpiresAt()
        );
    }

    private void validateForgotPassword(ForgotPassword forgotPassword) {
        if (forgotPassword.getVerifiedAt() != null) {
            throw new ForgotPasswordAlreadyVerifiedException();
        }

        if (!forgotPassword.getExpiresAt().isAfter(Instant.now())) {
            throw new ForgotPasswordExpiredException();
        }
    }
}
