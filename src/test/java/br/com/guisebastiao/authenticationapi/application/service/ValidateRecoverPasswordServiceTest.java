package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.command.ValidateRecoverPasswordCommand;
import br.com.guisebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import br.com.guisebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.SecureHasherPort;
import br.com.guisebastiao.authenticationapi.application.result.RecoverPasswordResult;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordAlreadyUsedException;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordExpiredException;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordNotFoundException;
import br.com.guisebastiao.authenticationapi.domain.exception.IncorrectOtpException;
import br.com.guisebastiao.authenticationapi.domain.model.Account;
import br.com.guisebastiao.authenticationapi.domain.model.RecoverPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ValidateRecoverPasswordServiceTest {

    @Mock
    private RecoverPasswordRepositoryPort recoverPasswordRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SecureHasherPort secureHasher;

    @InjectMocks
    private ValidateRecoverPasswordService service;

    @Test
    @DisplayName("Should verify the recovery request when the token and OTP are valid")
    void givenValidRecoveryTokenAndOtp_whenValidateRecoverPassword_thenRecoveryIsVerifiedAndReturned() {
        ValidateRecoverPasswordCommand command = new ValidateRecoverPasswordCommand("recover-token", "123456");
        String recoverTokenHash = "recover-token-hash";
        String otpCodeHash = "otp-code-hash";
        Instant now = Instant.now();
        Instant resendAvailableAt = now.plusSeconds(60);
        Instant expiresAt = now.plusSeconds(900);
        Account account = new Account();
        account.setEmail("user@example.com");
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setAccount(account);
        recoverPassword.setOtpCodeHash(otpCodeHash);
        recoverPassword.setResendAvailableAt(resendAvailableAt);
        recoverPassword.setExpiresAt(expiresAt);

        given(secureHasher.hash(command.recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        given(passwordEncoder.matches(command.otpCode(), otpCodeHash))
                .willReturn(true);

        given(recoverPasswordRepository.save(any(RecoverPassword.class)))
                .willReturn(recoverPassword);

        RecoverPasswordResult result = service.execute(command);

        assertEquals(account.getEmail(), result.email());
        assertEquals(command.recoverToken(), result.recoverToken());
        assertEquals(resendAvailableAt, result.resendAvailableAt());
        assertEquals(expiresAt, result.expiresAt());
        assertNotNull(recoverPassword.getVerifiedAt());

        then(secureHasher).should().hash(command.recoverToken());

        then(recoverPasswordRepository).should()
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        then(passwordEncoder).should().matches(command.otpCode(), otpCodeHash);
        then(recoverPasswordRepository).should().save(recoverPassword);
    }

    @Test
    @DisplayName("Should reject the request when the recovery token is not found")
    void givenUnknownRecoveryToken_whenValidateRecoverPassword_thenRecoveryNotFoundExceptionIsThrown() {
        ValidateRecoverPasswordCommand command = new ValidateRecoverPasswordCommand("unknown-recover-token", "123456");
        String recoverTokenHash = "unknown-recover-token-hash";

        given(secureHasher.hash(command.recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(Optional.empty());

        assertThrows(
                RecoverPasswordNotFoundException.class,
                () -> service.execute(command)
        );

        then(secureHasher).should().hash(command.recoverToken());

        then(recoverPasswordRepository).should()
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        then(passwordEncoder).shouldHaveNoInteractions();
        then(recoverPasswordRepository).should(never()).save(any(RecoverPassword.class));
    }

    @Test
    @DisplayName("Should reject the request when the recovery has expired")
    void givenExpiredRecovery_whenValidateRecoverPassword_thenRecoveryExpiredExceptionIsThrown() {
        ValidateRecoverPasswordCommand command = new ValidateRecoverPasswordCommand("expired-recover-token", "123456");
        String recoverTokenHash = "expired-recover-token-hash";
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setExpiresAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(command.recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        assertThrows(
                RecoverPasswordExpiredException.class,
                () -> service.execute(command)
        );

        then(recoverPasswordRepository).should()
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        then(passwordEncoder).shouldHaveNoInteractions();
        then(recoverPasswordRepository).should(never()).save(any(RecoverPassword.class));
    }

    @Test
    @DisplayName("Should reject the request when the recovery has already been used")
    void givenUsedRecovery_whenValidateRecoverPassword_thenRecoveryAlreadyUsedExceptionIsThrown() {
        ValidateRecoverPasswordCommand command = new ValidateRecoverPasswordCommand("used-recover-token", "123456");
        String recoverTokenHash = "used-recover-token-hash";
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setExpiresAt(Instant.now().plusSeconds(60));
        recoverPassword.setUsedAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(command.recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        assertThrows(
                RecoverPasswordAlreadyUsedException.class,
                () -> service.execute(command)
        );

        then(recoverPasswordRepository).should()
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        then(passwordEncoder).shouldHaveNoInteractions();
        then(recoverPasswordRepository).should(never()).save(any(RecoverPassword.class));
    }

    @Test
    @DisplayName("Should reject the request when the OTP is incorrect")
    void givenIncorrectOtp_whenValidateRecoverPassword_thenIncorrectOtpExceptionIsThrown() {
        ValidateRecoverPasswordCommand command = new ValidateRecoverPasswordCommand("recover-token", "654321");
        String recoverTokenHash = "recover-token-hash";
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setOtpCodeHash("otp-code-hash");
        recoverPassword.setExpiresAt(Instant.now().plusSeconds(60));

        given(secureHasher.hash(command.recoverToken()))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        given(passwordEncoder.matches(command.otpCode(), recoverPassword.getOtpCodeHash()))
                .willReturn(false);

        assertThrows(IncorrectOtpException.class, () -> service.execute(command));

        assertNull(recoverPassword.getVerifiedAt());

        then(passwordEncoder).should()
                .matches(command.otpCode(), recoverPassword.getOtpCodeHash());

        then(recoverPasswordRepository).should(never()).save(any(RecoverPassword.class));
    }
}
