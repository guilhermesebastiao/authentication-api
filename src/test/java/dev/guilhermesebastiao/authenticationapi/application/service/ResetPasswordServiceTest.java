package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoveryPasswordRepositoryPort;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.ResetPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoveryPasswordAlreadyUsedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ForgotPasswordExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoveryPasswordNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @Mock
    private RecoveryPasswordRepositoryPort recoverPasswordRepository;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private RevokeAllSessionsUseCase revokeAllSessionsUseCase;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SecureHasherPort secureHasher;

    @InjectMocks
    private ResetPasswordService service;

    @Test
    @DisplayName("Should reset the password and consume the verified recovery")
    void givenVerifiedActiveRecovery_whenResetPassword_thenPasswordIsChangedAndRecoveryIsUsed() {
        ResetPasswordInput command = new ResetPasswordInput("recover-token", "new-password", "new-password");
        String recoverTokenHash = "recover-token-hash";
        Account account = new Account();
        account.setPasswordHash("old-password-hash");
        RecoveryPassword recoverPassword = new RecoveryPassword();
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setAccount(account);
        recoverPassword.setForgotPassword(forgotPassword);
        recoverPassword.setExpiresAt(Instant.now().plusSeconds(600));

        given(secureHasher.hash(command.recoveryToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoveryTokenHash(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        given(passwordEncoder.hash(command.newPassword()))
                .willReturn("new-password-hash");

        given(accountRepository.save(account))
                .willReturn(account);

        given(recoverPasswordRepository.save(recoverPassword))
                .willReturn(recoverPassword);

        service.execute(command);

        assertEquals("new-password-hash", account.getPasswordHash());
        assertNotNull(recoverPassword.getUsedAt());

        then(secureHasher).should().hash(command.recoveryToken());
        then(recoverPasswordRepository).should().findByRecoveryTokenHash(recoverTokenHash);
        then(passwordEncoder).should().hash(command.newPassword());
        then(accountRepository).should().save(account);
        then(revokeAllSessionsUseCase).should().execute(account);
        then(recoverPasswordRepository).should().save(recoverPassword);
    }

    @Test
    @DisplayName("Should reject an unknown recovery token")
    void givenUnknownRecoveryToken_whenResetPassword_thenThrowNotFoundException() {
        ResetPasswordInput command = command();
        String recoverTokenHash = "unknown-recover-token-hash";

        given(secureHasher.hash(command.recoveryToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoveryTokenHash(recoverTokenHash))
                .willReturn(Optional.empty());

        assertThrows(RecoveryPasswordNotFoundException.class, () -> service.execute(command));

        then(passwordEncoder).shouldHaveNoInteractions();
        then(accountRepository).shouldHaveNoInteractions();
        then(revokeAllSessionsUseCase).shouldHaveNoInteractions();
        then(recoverPasswordRepository).should().findByRecoveryTokenHash(recoverTokenHash);
        then(recoverPasswordRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Should reject an expired recovery")
    void givenExpiredRecovery_whenResetPassword_thenThrowExpiredException() {
        String recoverTokenHash = "expired-recover-token-hash";

        RecoveryPassword recoverPassword = recovery(
                Instant.now().minusSeconds(1),
                Instant.now().minusSeconds(60),
                null
        );

        given(secureHasher.hash(command().recoveryToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoveryTokenHash(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        assertThrows(dev.guilhermesebastiao.authenticationapi.domain.exception.RecoveryPasswordExpiredException.class, () -> service.execute(command()));

        then(passwordEncoder).shouldHaveNoInteractions();
        then(accountRepository).shouldHaveNoInteractions();
        then(revokeAllSessionsUseCase).shouldHaveNoInteractions();
        then(recoverPasswordRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Should reject an already used recovery")
    void givenUsedRecovery_whenResetPassword_thenThrowAlreadyUsedException() {
        String recoverTokenHash = "used-recover-token-hash";

        RecoveryPassword recoverPassword = recovery(
                Instant.now().plusSeconds(600),
                Instant.now().minusSeconds(60),
                Instant.now().minusSeconds(30)
        );

        given(secureHasher.hash(command().recoveryToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoveryTokenHash(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        assertThrows(RecoveryPasswordAlreadyUsedException.class, () -> service.execute(command()));

        then(passwordEncoder).shouldHaveNoInteractions();
        then(accountRepository).shouldHaveNoInteractions();
        then(revokeAllSessionsUseCase).shouldHaveNoInteractions();
        then(recoverPasswordRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Should reject an unverified recovery")
    void givenUnverifiedRecovery_whenResetPassword_thenThrowNotVerifiedException() {
        String recoverTokenHash = "unverified-recover-token-hash";

        RecoveryPassword recoverPassword = recovery(
                Instant.now().plusSeconds(600),
                null,
                null
        );

        given(secureHasher.hash(command().recoveryToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoveryTokenHash(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        service.execute(command());
        then(accountRepository).should().save(recoverPassword.getForgotPassword().getAccount());

        then(passwordEncoder).should().hash("new-password");
        then(accountRepository).should().save(recoverPassword.getForgotPassword().getAccount());
        then(revokeAllSessionsUseCase).should().execute(recoverPassword.getForgotPassword().getAccount());
        then(recoverPasswordRepository).should().save(recoverPassword);
    }

    private ResetPasswordInput command() {
        return new ResetPasswordInput("recover-token", "new-password", "new-password");
    }

    private RecoveryPassword recovery(Instant expiresAt, Instant verifiedAt, Instant usedAt) {
        RecoveryPassword recoverPassword = new RecoveryPassword();
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setAccount(new Account());
        recoverPassword.setForgotPassword(forgotPassword);
        recoverPassword.setExpiresAt(expiresAt);
        recoverPassword.setUsedAt(usedAt);
        return recoverPassword;
    }
}
