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
class SignOutAllServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepository;

    @Mock
    private RefreshRepositoryPort refreshRepository;

    @InjectMocks
    private RevokeAllSessionsService service;

    @Test
    @DisplayName("Should revoke all active sessions and refresh tokens of the account")
    void givenActiveSessionsAndRefreshTokens_whenSignOutAll_thenAllCredentialsAreRevoked() {
        Account account = new Account();
        account.setId(UUID.randomUUID());

        Session firstSession = new Session();
        firstSession.setId(UUID.randomUUID());
        Session secondSession = new Session();
        secondSession.setId(UUID.randomUUID());
        List<Session> sessions = List.of(firstSession, secondSession);

        Refresh firstRefresh = new Refresh();
        firstRefresh.setSession(firstSession);
        Refresh secondRefresh = new Refresh();
        secondRefresh.setSession(secondSession);
        List<Refresh> refreshes = List.of(firstRefresh, secondRefresh);

        given(sessionRepository.findAllByAccountIdAndNotRevoked(account.getId()))
                .willReturn(sessions);

        given(refreshRepository.findAllBySessionIdsAndNotRevoked(
                List.of(firstSession.getId(), secondSession.getId())
        )).willReturn(refreshes);

        service.execute(account);

        assertNotNull(firstSession.getRevokedAt());
        assertNotNull(secondSession.getRevokedAt());
        assertNotNull(firstRefresh.getRevokedAt());
        assertNotNull(secondRefresh.getRevokedAt());
        assertSame(firstSession.getRevokedAt(), secondSession.getRevokedAt());
        assertSame(firstSession.getRevokedAt(), firstRefresh.getRevokedAt());
        assertSame(firstSession.getRevokedAt(), secondRefresh.getRevokedAt());

        then(sessionRepository).should()
                .findAllByAccountIdAndNotRevoked(account.getId());
        then(refreshRepository).should()
                .findAllBySessionIdsAndNotRevoked(List.of(firstSession.getId(), secondSession.getId()));
        then(refreshRepository).should().save(refreshes);
        then(sessionRepository).should().save(sessions);
    }

    @Test
    @DisplayName("Should return without querying refresh tokens when the account has no active sessions")
    void givenNoActiveSessions_whenSignOutAll_thenNoRefreshTokensAreQueriedOrSaved() {
        Account account = new Account();
        account.setId(UUID.randomUUID());

        given(sessionRepository.findAllByAccountIdAndNotRevoked(account.getId()))
                .willReturn(List.of());

        service.execute(account);

        then(sessionRepository).should()
                .findAllByAccountIdAndNotRevoked(account.getId());
        then(sessionRepository).should(never()).save(anyList());
        then(refreshRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should revoke and save sessions when there are no active refresh tokens")
    void givenActiveSessionsAndNoActiveRefreshTokens_whenSignOutAll_thenSessionsAreRevokedAndSaved() {
        Account account = new Account();
        account.setId(UUID.randomUUID());

        Session firstSession = new Session();
        firstSession.setId(UUID.randomUUID());
        Session secondSession = new Session();
        secondSession.setId(UUID.randomUUID());
        List<Session> sessions = List.of(firstSession, secondSession);

        given(sessionRepository.findAllByAccountIdAndNotRevoked(account.getId()))
                .willReturn(sessions);

        given(refreshRepository.findAllBySessionIdsAndNotRevoked(
                List.of(firstSession.getId(), secondSession.getId())
        )).willReturn(List.of());

        service.execute(account);

        assertNotNull(firstSession.getRevokedAt());
        assertNotNull(secondSession.getRevokedAt());
        assertSame(firstSession.getRevokedAt(), secondSession.getRevokedAt());

        then(refreshRepository).should()
                .findAllBySessionIdsAndNotRevoked(List.of(firstSession.getId(), secondSession.getId()));

        then(refreshRepository).should(never()).save(anyList());
        then(sessionRepository).should().save(sessions);
    }
}
