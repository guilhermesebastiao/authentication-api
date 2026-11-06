package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.ResetPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoveryPasswordAlreadyUsedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ForgotPasswordExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoveryPasswordNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
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
    private RecoverPasswordRepositoryPort recoverPasswordRepository;

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
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setAccount(account);
        recoverPassword.setExpiresAt(Instant.now().plusSeconds(600));
        recoverPassword.setVerifiedAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(command.recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
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

        then(secureHasher).should().hash(command.recoverToken());
        then(recoverPasswordRepository).should().findByRecoverTokenHash(recoverTokenHash);
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

        given(secureHasher.hash(command.recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.empty());

        assertThrows(RecoveryPasswordNotFoundException.class, () -> service.execute(command));

        then(passwordEncoder).shouldHaveNoInteractions();
        then(accountRepository).shouldHaveNoInteractions();
        then(revokeAllSessionsUseCase).shouldHaveNoInteractions();
        then(recoverPasswordRepository).should().findByRecoverTokenHash(recoverTokenHash);
        then(recoverPasswordRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Should reject an expired recovery")
    void givenExpiredRecovery_whenResetPassword_thenThrowExpiredException() {
        String recoverTokenHash = "expired-recover-token-hash";

        RecoverPassword recoverPassword = recovery(
                Instant.now().minusSeconds(1),
                Instant.now().minusSeconds(60),
                null
        );

        given(secureHasher.hash(command().recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        assertThrows(ForgotPasswordExpiredException.class, () -> service.execute(command()));

        then(passwordEncoder).shouldHaveNoInteractions();
        then(accountRepository).shouldHaveNoInteractions();
        then(revokeAllSessionsUseCase).shouldHaveNoInteractions();
        then(recoverPasswordRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Should reject an already used recovery")
    void givenUsedRecovery_whenResetPassword_thenThrowAlreadyUsedException() {
        String recoverTokenHash = "used-recover-token-hash";

        RecoverPassword recoverPassword = recovery(
                Instant.now().plusSeconds(600),
                Instant.now().minusSeconds(60),
                Instant.now().minusSeconds(30)
        );

        given(secureHasher.hash(command().recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
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

        RecoverPassword recoverPassword = recovery(
                Instant.now().plusSeconds(600),
                null,
                null
        );

        given(secureHasher.hash(command().recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        assertThrows(RecoverPasswordNotVerifiedException.class, () -> service.execute(command()));

        then(passwordEncoder).shouldHaveNoInteractions();
        then(accountRepository).shouldHaveNoInteractions();
        then(revokeAllSessionsUseCase).shouldHaveNoInteractions();
        then(recoverPasswordRepository).shouldHaveNoMoreInteractions();
    }

    private ResetPasswordInput command() {
        return new ResetPasswordInput("recover-token", "new-password", "new-password");
    }

    private RecoverPassword recovery(Instant expiresAt, Instant verifiedAt, Instant usedAt) {
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setAccount(new Account());
        recoverPassword.setExpiresAt(expiresAt);
        recoverPassword.setVerifiedAt(verifiedAt);
        recoverPassword.setUsedAt(usedAt);
        return recoverPassword;
    }
}
