package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.command.ResetPasswordCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SignOutAllUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordAlreadyUsedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoverPasswordNotVerifiedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoverPassword;
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
    private SignOutAllUseCase signOutAllUseCase;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SecureHasherPort secureHasher;

    @InjectMocks
    private ResetPasswordService service;

    @Test
    @DisplayName("Should reset the password and consume the verified recovery")
    void givenVerifiedActiveRecovery_whenResetPassword_thenPasswordIsChangedAndRecoveryIsUsed() {
        ResetPasswordCommand command = new ResetPasswordCommand("recover-token", "new-password", "new-password");
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
        then(signOutAllUseCase).should().execute(account);
        then(recoverPasswordRepository).should().save(recoverPassword);
    }

    @Test
    @DisplayName("Should reject an unknown recovery token")
    void givenUnknownRecoveryToken_whenResetPassword_thenThrowNotFoundException() {
        ResetPasswordCommand command = command();
        String recoverTokenHash = "unknown-recover-token-hash";

        given(secureHasher.hash(command.recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.empty());

        assertThrows(RecoverPasswordNotFoundException.class, () -> service.execute(command));

        then(passwordEncoder).shouldHaveNoInteractions();
        then(accountRepository).shouldHaveNoInteractions();
        then(signOutAllUseCase).shouldHaveNoInteractions();
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

        assertThrows(RecoverPasswordExpiredException.class, () -> service.execute(command()));

        then(passwordEncoder).shouldHaveNoInteractions();
        then(accountRepository).shouldHaveNoInteractions();
        then(signOutAllUseCase).shouldHaveNoInteractions();
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

        assertThrows(RecoverPasswordAlreadyUsedException.class, () -> service.execute(command()));

        then(passwordEncoder).shouldHaveNoInteractions();
        then(accountRepository).shouldHaveNoInteractions();
        then(signOutAllUseCase).shouldHaveNoInteractions();
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
        then(signOutAllUseCase).shouldHaveNoInteractions();
        then(recoverPasswordRepository).shouldHaveNoMoreInteractions();
    }

    private ResetPasswordCommand command() {
        return new ResetPasswordCommand("recover-token", "new-password", "new-password");
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
