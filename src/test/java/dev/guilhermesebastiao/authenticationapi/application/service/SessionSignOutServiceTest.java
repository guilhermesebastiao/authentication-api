package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SessionSignOutServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepository;

    @Mock
    private RefreshRepositoryPort refreshRepository;

    @InjectMocks
    private RevokeSessionsService service;

    @Test
    @DisplayName("Should revoke only authorized sessions and their active refresh tokens")
    void givenAuthorizedActiveSessions_whenSignOutSessions_thenSessionsAndRefreshTokensAreRevoked() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        UUID authorizedSessionId = UUID.randomUUID();
        UUID unavailableSessionId = UUID.randomUUID();
        List<UUID> requestedSessionIds = List.of(authorizedSessionId, unavailableSessionId);

        Session session = new Session();
        session.setId(authorizedSessionId);
        Refresh refresh = new Refresh();
        refresh.setSession(session);
        List<Session> sessions = List.of(session);
        List<Refresh> refreshes = List.of(refresh);

        given(sessionRepository.findAllByAccountIdAndSessionIdsAndNotRevoked(
                account.getId(), requestedSessionIds
        )).willReturn(sessions);

        given(refreshRepository.findAllBySessionIdsAndNotRevoked(List.of(authorizedSessionId)))
                .willReturn(refreshes);

        service.execute(account, requestedSessionIds);

        assertNotNull(session.getRevokedAt());
        assertNotNull(refresh.getRevokedAt());
        assertSame(session.getRevokedAt(), refresh.getRevokedAt());

        then(sessionRepository).should()
                .findAllByAccountIdAndSessionIdsAndNotRevoked(account.getId(), requestedSessionIds);
        then(refreshRepository).should()
                .findAllBySessionIdsAndNotRevoked(List.of(authorizedSessionId));
        then(refreshRepository).should().save(refreshes);
        then(sessionRepository).should().save(sessions);
    }

    @Test
    @DisplayName("Should return without processing when no authorized sessions are found")
    void givenNoAuthorizedSessions_whenSignOutSessions_thenNoSessionsOrRefreshTokensAreSaved() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        List<UUID> requestedSessionIds = List.of(UUID.randomUUID());

        given(sessionRepository.findAllByAccountIdAndSessionIdsAndNotRevoked(
                account.getId(), requestedSessionIds
        )).willReturn(List.of());

        service.execute(account, requestedSessionIds);

        then(sessionRepository).should()
                .findAllByAccountIdAndSessionIdsAndNotRevoked(account.getId(), requestedSessionIds);
        then(sessionRepository).should(never()).save(anyList());
        then(refreshRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should revoke and save authorized sessions without active refresh tokens")
    void givenAuthorizedSessionsWithoutRefreshTokens_whenSignOutSessions_thenSessionsAreRevokedAndSaved() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        UUID sessionId = UUID.randomUUID();
        List<UUID> requestedSessionIds = List.of(sessionId);
        Session session = new Session();
        session.setId(sessionId);
        List<Session> sessions = List.of(session);

        given(sessionRepository.findAllByAccountIdAndSessionIdsAndNotRevoked(
                account.getId(), requestedSessionIds
        )).willReturn(sessions);

        given(refreshRepository.findAllBySessionIdsAndNotRevoked(List.of(sessionId)))
                .willReturn(List.of());

        service.execute(account, requestedSessionIds);

        assertNotNull(session.getRevokedAt());
        then(refreshRepository).should()
                .findAllBySessionIdsAndNotRevoked(List.of(sessionId));
        then(refreshRepository).should().save(List.of());
        then(sessionRepository).should().save(sessions);
    }
}
