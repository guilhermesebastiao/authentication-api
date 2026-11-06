package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionRevokedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SignOutServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepository;

    @Mock
    private RefreshRepositoryPort refreshRepository;

    @Mock
    private SecureHasherPort secureHasher;

    @InjectMocks
    private SignOutService service;

    @Test
    @DisplayName("Should revoke the session and its active refresh tokens")
    void givenActiveSessionAndRefreshTokens_whenSignOut_thenSessionAndRefreshTokensAreRevoked() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        String sessionToken = "session-token";
        String sessionTokenHash = "session-token-hash";

        Session session = new Session();
        session.setId(UUID.randomUUID());
        session.setExpiresAt(Instant.now().plusSeconds(60));

        Refresh refresh = new Refresh();
        refresh.setSession(session);
        List<Refresh> refreshes = List.of(refresh);

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(sessionRepository.findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash))
                .willReturn(java.util.Optional.of(session));

        given(refreshRepository.findAllBySessionIdsAndNotRevoked(List.of(session.getId())))
                .willReturn(refreshes);

        service.execute(account, sessionToken);

        assertNotNull(refresh.getRevokedAt());
        assertNotNull(session.getRevokedAt());

        then(secureHasher).should().hash(sessionToken);
        then(sessionRepository).should()
                .findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash);

        then(refreshRepository).should()
                .findAllBySessionIdsAndNotRevoked(List.of(session.getId()));

        then(refreshRepository).should().save(refreshes);
        then(sessionRepository).should().save(session);
    }

    @Test
    @DisplayName("Should revoke and save the session when it has no active refresh tokens")
    void givenActiveSessionWithoutRefreshTokens_whenSignOut_thenSessionIsRevokedAndSaved() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        String sessionToken = "session-token";
        String sessionTokenHash = "session-token-hash";

        Session session = new Session();
        session.setId(UUID.randomUUID());
        session.setExpiresAt(Instant.now().plusSeconds(60));

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(sessionRepository.findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash))
                .willReturn(java.util.Optional.of(session));

        given(refreshRepository.findAllBySessionIdsAndNotRevoked(List.of(session.getId())))
                .willReturn(List.of());

        service.execute(account, sessionToken);

        assertNotNull(session.getRevokedAt());

        then(refreshRepository).should()
                .findAllBySessionIdsAndNotRevoked(List.of(session.getId()));

        then(refreshRepository).should().save(List.of());
        then(sessionRepository).should().save(session);
    }

    @Test
    @DisplayName("Should reject sign out when the session does not belong to the account")
    void givenUnknownSessionToken_whenSignOut_thenSessionNotFoundExceptionIsThrown() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        String sessionToken = "unknown-session-token";
        String sessionTokenHash = "unknown-session-token-hash";

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(sessionRepository.findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash))
                .willReturn(java.util.Optional.empty());

        assertThrows(SessionNotFoundException.class, () -> service.execute(account, sessionToken));

        then(secureHasher).should().hash(sessionToken);

        then(sessionRepository).should()
                .findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash);

        then(sessionRepository).should(never()).save(any(Session.class));
        then(refreshRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject sign out when the session has expired")
    void givenExpiredSession_whenSignOut_thenSessionExpiredExceptionIsThrown() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        String sessionToken = "expired-session-token";
        String sessionTokenHash = "expired-session-token-hash";

        Session session = new Session();
        session.setId(UUID.randomUUID());
        session.setExpiresAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(sessionRepository.findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash))
                .willReturn(java.util.Optional.of(session));

        assertThrows(SessionExpiredException.class, () -> service.execute(account, sessionToken));

        then(sessionRepository).should()
                .findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash);

        then(sessionRepository).should(never()).save(any(Session.class));
        then(refreshRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject sign out when the session has already been revoked")
    void givenRevokedSession_whenSignOut_thenSessionRevokedExceptionIsThrown() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        String sessionToken = "revoked-session-token";
        String sessionTokenHash = "revoked-session-token-hash";

        Session session = new Session();
        session.setId(UUID.randomUUID());
        session.setExpiresAt(Instant.now().plusSeconds(60));
        session.setRevokedAt(Instant.now().minusSeconds(60));

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(sessionRepository.findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash))
                .willReturn(java.util.Optional.of(session));

        assertThrows(SessionRevokedException.class, () -> service.execute(account, sessionToken));

        then(sessionRepository).should()
                .findByAccountIdAndSessionTokenHash(account.getId(), sessionTokenHash);

        then(sessionRepository).should(never()).save(any(Session.class));
        then(refreshRepository).shouldHaveNoInteractions();
    }
}
