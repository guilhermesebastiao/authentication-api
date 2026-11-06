package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionRevokedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ValidateSessionServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepository;

    @Mock
    private SecureHasherPort secureHasher;

    @InjectMocks
    private ValidateSessionService service;

    @Test
    @DisplayName("Should return the session when its token is valid and it has not expired or been revoked")
    void givenValidSessionToken_whenValidateSession_thenSessionIsReturned() {
        String sessionToken = "session-token";
        String sessionTokenHash = "session-token-hash";
        Session session = new Session();
        session.setExpiresAt(Instant.now().plusSeconds(60));

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(sessionRepository.findBySessionTokenHash(sessionTokenHash))
                .willReturn(Optional.of(session));

        Session result = service.execute(sessionToken);

        assertSame(session, result);
        then(secureHasher).should().hash(sessionToken);
        then(sessionRepository).should().findBySessionTokenHash(sessionTokenHash);
    }

    @Test
    @DisplayName("Should reject the session when its token is not found")
    void givenUnknownSessionToken_whenValidateSession_thenSessionNotFoundExceptionIsThrown() {
        String sessionToken = "unknown-session-token";
        String sessionTokenHash = "unknown-session-token-hash";

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(sessionRepository.findBySessionTokenHash(sessionTokenHash))
                .willReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, () -> service.execute(sessionToken));

        then(secureHasher).should().hash(sessionToken);
        then(sessionRepository).should().findBySessionTokenHash(sessionTokenHash);
    }

    @Test
    @DisplayName("Should reject the session when it has been revoked")
    void givenRevokedSession_whenValidateSession_thenSessionRevokedExceptionIsThrown() {
        String sessionToken = "revoked-session-token";
        String sessionTokenHash = "revoked-session-token-hash";
        Session session = new Session();
        session.setRevokedAt(Instant.now().minusSeconds(60));
        session.setExpiresAt(Instant.now().plusSeconds(60));

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(sessionRepository.findBySessionTokenHash(sessionTokenHash))
                .willReturn(Optional.of(session));

        assertThrows(SessionRevokedException.class, () -> service.execute(sessionToken));

        then(secureHasher).should().hash(sessionToken);
        then(sessionRepository).should().findBySessionTokenHash(sessionTokenHash);
    }

    @Test
    @DisplayName("Should reject the session when it has expired")
    void givenExpiredSession_whenValidateSession_thenSessionExpiredExceptionIsThrown() {
        String sessionToken = "expired-session-token";
        String sessionTokenHash = "expired-session-token-hash";
        Session session = new Session();
        session.setExpiresAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(sessionRepository.findBySessionTokenHash(sessionTokenHash))
                .willReturn(Optional.of(session));

        assertThrows(SessionExpiredException.class, () -> service.execute(sessionToken));

        then(secureHasher).should().hash(sessionToken);
        then(sessionRepository).should().findBySessionTokenHash(sessionTokenHash);
    }
}
