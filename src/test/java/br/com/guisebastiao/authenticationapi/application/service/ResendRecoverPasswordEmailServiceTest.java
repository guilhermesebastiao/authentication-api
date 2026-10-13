package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.port.in.SendRecoverPasswordEmailUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import br.com.guisebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.SecureHasherPort;
import br.com.guisebastiao.authenticationapi.application.result.RecoverPasswordResult;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordAlreadyVerifiedException;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordExpiredException;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordNotFoundException;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordResendNotAvailableException;
import br.com.guisebastiao.authenticationapi.domain.model.Account;
import br.com.guisebastiao.authenticationapi.domain.model.RecoverPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ResendRecoverPasswordEmailServiceTest {

    @Mock
    private SendRecoverPasswordEmailUseCase sendRecoverPasswordEmailUseCase;

    @Mock
    private RecoverPasswordRepositoryPort recoverPasswordRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SecureHasherPort secureHasher;

    @Mock
    private SecureRandom secureRandom;

    @InjectMocks
    private ResendRecoverPasswordEmailService service;

    @Test
    @DisplayName("Should generate and send a new OTP for an active recovery")
    void givenActiveRecovery_whenResendRecoveryEmail_thenOtpIsRegeneratedAndEmailIsSent() {
        String recoverToken = "recover-token";
        String recoverTokenHash = "recover-token-hash";
        Account account = new Account();
        account.setEmail("user@example.com");
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setAccount(account);
        recoverPassword.setOtpCodeHash("old-otp-code-hash");
        recoverPassword.setExpiresAt(Instant.now().plusSeconds(600));
        recoverPassword.setResendAvailableAt(Instant.now().minusSeconds(60));
        Instant beforeExecution = Instant.now();

        given(secureHasher.hash(recoverToken))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(java.util.Optional.of(recoverPassword));

        given(secureRandom.nextInt(1_000_000))
                .willReturn(1234);

        given(passwordEncoder.hash("001234"))
                .willReturn("new-otp-code-hash");

        given(recoverPasswordRepository.save(recoverPassword))
                .willReturn(recoverPassword);

        RecoverPasswordResult result = service.execute(recoverToken);
        Instant afterExecution = Instant.now();

        assertEquals(account.getEmail(), result.email());
        assertEquals(recoverToken, result.recoverToken());
        assertEquals("new-otp-code-hash", recoverPassword.getOtpCodeHash());
        assertNotNull(recoverPassword.getResendAvailableAt());
        assertFalse(recoverPassword.getResendAvailableAt().isBefore(beforeExecution.plusSeconds(30)));
        assertTrue(recoverPassword.getResendAvailableAt().isBefore(afterExecution.plusSeconds(90)));
        assertEquals(recoverPassword.getResendAvailableAt(), result.resendAvailableAt());
        assertEquals(recoverPassword.getExpiresAt(), result.expiresAt());

        then(secureHasher).should().hash(recoverToken);

        then(recoverPasswordRepository).should()
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        then(secureRandom).should().nextInt(1_000_000);
        then(passwordEncoder).should().hash("001234");
        then(recoverPasswordRepository).should().save(recoverPassword);

        then(sendRecoverPasswordEmailUseCase).should()
                .execute(account.getEmail(), "001234", recoverPassword.getExpiresAt());
    }

    @Test
    @DisplayName("Should reject the request when the recovery token is not found")
    void givenUnknownRecoveryToken_whenResendRecoveryEmail_thenRecoveryNotFoundExceptionIsThrown() {
        String recoverToken = "unknown-recover-token";
        String recoverTokenHash = "unknown-recover-token-hash";

        given(secureHasher.hash(recoverToken))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(java.util.Optional.empty());

        assertThrows(RecoverPasswordNotFoundException.class, () -> service.execute(recoverToken));

        then(secureHasher).should().hash(recoverToken);

        then(recoverPasswordRepository).should()
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        then(recoverPasswordRepository).should(never()).save(any(RecoverPassword.class));
        then(passwordEncoder).shouldHaveNoInteractions();
        then(secureRandom).shouldHaveNoInteractions();
        then(sendRecoverPasswordEmailUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject the request when the recovery has already been verified")
    void givenVerifiedRecovery_whenResendRecoveryEmail_thenRecoveryAlreadyVerifiedExceptionIsThrown() {
        String recoverToken = "verified-recover-token";
        String recoverTokenHash = "verified-recover-token-hash";
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setVerifiedAt(Instant.now().minusSeconds(60));
        recoverPassword.setExpiresAt(Instant.now().plusSeconds(600));
        recoverPassword.setResendAvailableAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(recoverToken))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(java.util.Optional.of(recoverPassword));

        assertThrows(RecoverPasswordAlreadyVerifiedException.class, () -> service.execute(recoverToken));

        then(recoverPasswordRepository).should()
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        then(recoverPasswordRepository).should(never()).save(any(RecoverPassword.class));
        then(passwordEncoder).shouldHaveNoInteractions();
        then(secureRandom).shouldHaveNoInteractions();
        then(sendRecoverPasswordEmailUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject the request when the recovery has expired")
    void givenExpiredRecovery_whenResendRecoveryEmail_thenRecoveryExpiredExceptionIsThrown() {
        String recoverToken = "expired-recover-token";
        String recoverTokenHash = "expired-recover-token-hash";
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setExpiresAt(Instant.now().minusSeconds(60));
        recoverPassword.setResendAvailableAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(recoverToken))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(java.util.Optional.of(recoverPassword));

        assertThrows(RecoverPasswordExpiredException.class, () -> service.execute(recoverToken));

        then(recoverPasswordRepository).should()
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        then(recoverPasswordRepository).should(never()).save(any(RecoverPassword.class));
        then(passwordEncoder).shouldHaveNoInteractions();
        then(secureRandom).shouldHaveNoInteractions();
        then(sendRecoverPasswordEmailUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject the request when resending is not available yet")
    void givenRecoveryBeforeResendWindow_whenResendRecoveryEmail_thenResendNotAvailableExceptionIsThrown() {
        String recoverToken = "not-available-recover-token";
        String recoverTokenHash = "not-available-recover-token-hash";
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setExpiresAt(Instant.now().plusSeconds(600));
        recoverPassword.setResendAvailableAt(Instant.now().plusSeconds(60));

        given(secureHasher.hash(recoverToken))
                .willReturn(recoverTokenHash);

        given(recoverPasswordRepository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(java.util.Optional.of(recoverPassword));

        assertThrows(
                RecoverPasswordResendNotAvailableException.class,
                () -> service.execute(recoverToken)
        );

        then(recoverPasswordRepository).should()
                .findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        then(recoverPasswordRepository).should(never()).save(any(RecoverPassword.class));
        then(passwordEncoder).shouldHaveNoInteractions();
        then(secureRandom).shouldHaveNoInteractions();
        then(sendRecoverPasswordEmailUseCase).shouldHaveNoInteractions();
    }
}
