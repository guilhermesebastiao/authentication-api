package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.RecoveryPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoveryPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ForgotPasswordExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoveryPasswordAlreadyUsedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RecoveryPasswordNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GetRecoveryPasswordServiceTest {
    @Mock private RecoveryPasswordRepositoryPort recoveryPasswordRepository;
    @Mock private SecureHasherPort secureHasher;
    @InjectMocks private GetRecoveryPasswordService service;

    @Test
    void returnsActiveRecovery() {
        String token = "recovery-token", hash = "token-hash";
        Instant expiry = Instant.now().plusSeconds(600);
        RecoveryPassword recovery = new RecoveryPassword();
        recovery.setExpiresAt(expiry);
        given(secureHasher.hash(token)).willReturn(hash);
        given(recoveryPasswordRepository.findByRecoveryTokenHash(hash)).willReturn(Optional.of(recovery));

        RecoveryPasswordOutput result = service.execute(token);

        assertEquals(token, result.recoveryToken());
        assertEquals(expiry, result.expiresAt());
        assertFalse(result.usedAt());
        then(recoveryPasswordRepository).should().findByRecoveryTokenHash(hash);
    }

    @Test
    void rejectsUnknownToken() {
        given(secureHasher.hash("unknown")).willReturn("hash");
        given(recoveryPasswordRepository.findByRecoveryTokenHash("hash")).willReturn(Optional.empty());
        assertThrows(RecoveryPasswordNotFoundException.class, () -> service.execute("unknown"));
    }

    @Test
    void rejectsExpiredRecovery() {
        RecoveryPassword recovery = new RecoveryPassword();
        recovery.setExpiresAt(Instant.now().minusSeconds(1));
        given(secureHasher.hash("expired")).willReturn("hash");
        given(recoveryPasswordRepository.findByRecoveryTokenHash("hash")).willReturn(Optional.of(recovery));
        assertThrows(ForgotPasswordExpiredException.class, () -> service.execute("expired"));
    }

    @Test
    void rejectsUsedRecovery() {
        RecoveryPassword recovery = new RecoveryPassword();
        recovery.setExpiresAt(Instant.now().plusSeconds(60));
        recovery.setUsedAt(Instant.now());
        given(secureHasher.hash("used")).willReturn("hash");
        given(recoveryPasswordRepository.findByRecoveryTokenHash("hash")).willReturn(Optional.of(recovery));
        assertThrows(RecoveryPasswordAlreadyUsedException.class, () -> service.execute("used"));
    }
}
