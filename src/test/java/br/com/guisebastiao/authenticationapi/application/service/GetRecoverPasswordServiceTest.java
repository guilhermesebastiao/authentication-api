package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.SecureHasherPort;
import br.com.guisebastiao.authenticationapi.application.result.RecoverPasswordResult;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordAlreadyUsedException;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordExpiredException;
import br.com.guisebastiao.authenticationapi.domain.exception.RecoverPasswordNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GetRecoverPasswordServiceTest {

    @Mock
    private RecoverPasswordRepositoryPort recoverPasswordRepository;

    @Mock
    private SecureHasherPort secureHasher;

    @InjectMocks
    private GetRecoverPasswordService service;

    @Test
    @DisplayName("Should return an active password recovery")
    void givenActiveRecovery_whenGetRecoverPassword_thenRecoveryIsReturned() {
        String recoverToken = "recover-token";
        String recoverTokenHash = "recover-token-hash";
        Instant resendAvailableAt = Instant.now().plusSeconds(60);
        Instant expiresAt = Instant.now().plusSeconds(600);
        Account account = new Account();
        account.setEmail("user@example.com");
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setAccount(account);
        recoverPassword.setResendAvailableAt(resendAvailableAt);
        recoverPassword.setExpiresAt(expiresAt);

        given(secureHasher.hash(recoverToken)).willReturn(recoverTokenHash);
        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        RecoverPasswordResult result = service.execute(recoverToken);

        assertEquals(account.getEmail(), result.email());
        assertEquals(recoverToken, result.recoverToken());
        assertEquals(resendAvailableAt, result.resendAvailableAt());
        assertEquals(expiresAt, result.expiresAt());
        then(secureHasher).should().hash(recoverToken);
        then(recoverPasswordRepository).should().findByRecoverTokenHash(recoverTokenHash);
    }

    @Test
    @DisplayName("Should reject an unknown password recovery token")
    void givenUnknownRecoveryToken_whenGetRecoverPassword_thenRecoveryNotFoundExceptionIsThrown() {
        String recoverToken = "unknown-recover-token";
        String recoverTokenHash = "unknown-recover-token-hash";

        given(secureHasher.hash(recoverToken)).willReturn(recoverTokenHash);
        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.empty());

        assertThrows(RecoverPasswordNotFoundException.class, () -> service.execute(recoverToken));

        then(recoverPasswordRepository).should().findByRecoverTokenHash(recoverTokenHash);
    }

    @Test
    @DisplayName("Should reject an expired password recovery")
    void givenExpiredRecovery_whenGetRecoverPassword_thenRecoveryExpiredExceptionIsThrown() {
        String recoverToken = "expired-recover-token";
        String recoverTokenHash = "expired-recover-token-hash";
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setExpiresAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(recoverToken)).willReturn(recoverTokenHash);
        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        assertThrows(RecoverPasswordExpiredException.class, () -> service.execute(recoverToken));

        then(recoverPasswordRepository).should().findByRecoverTokenHash(recoverTokenHash);
    }

    @Test
    @DisplayName("Should reject a used password recovery")
    void givenUsedRecovery_whenGetRecoverPassword_thenRecoveryAlreadyUsedExceptionIsThrown() {
        String recoverToken = "used-recover-token";
        String recoverTokenHash = "used-recover-token-hash";
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setExpiresAt(Instant.now().plusSeconds(600));
        recoverPassword.setUsedAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(recoverToken)).willReturn(recoverTokenHash);
        given(recoverPasswordRepository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.of(recoverPassword));

        assertThrows(RecoverPasswordAlreadyUsedException.class, () -> service.execute(recoverToken));

        then(recoverPasswordRepository).should().findByRecoverTokenHash(recoverTokenHash);
    }
}
