package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageQuery;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageResult;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GetSessionsServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepository;

    @Mock
    private SecureHasherPort secureHasher;

    @InjectMocks
    private GetSessionsService service;

    @Test
    @DisplayName("Should return paginated sessions and identify the current session")
    void givenActiveSessionsAndCurrentToken_whenGetSessions_thenSessionsAreMappedWithCurrentFlag() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        String currentSessionToken = "current-session-token";
        String currentSessionTokenHash = "current-session-token-hash";
        PageQuery paging = new PageQuery(1, 10);
        Instant firstLastSeenAt = Instant.parse("2026-10-04T10:00:00Z");
        Instant firstCreatedAt = Instant.parse("2026-10-01T10:00:00Z");
        Instant secondLastSeenAt = Instant.parse("2026-10-03T10:00:00Z");
        Instant secondCreatedAt = Instant.parse("2026-09-30T10:00:00Z");

        Session currentSession = new Session();
        currentSession.setId(UUID.randomUUID());
        currentSession.setType(DeviceType.DESKTOP);
        currentSession.setLastSeenAt(firstLastSeenAt);
        currentSession.setLocation("Sao Paulo, Brazil");
        currentSession.setSessionTokenHash(currentSessionTokenHash);
        currentSession.setCreatedAt(firstCreatedAt);

        Session otherSession = new Session();
        otherSession.setId(UUID.randomUUID());
        otherSession.setType(DeviceType.MOBILE);
        otherSession.setLastSeenAt(secondLastSeenAt);
        otherSession.setLocation("Rio de Janeiro, Brazil");
        otherSession.setSessionTokenHash("other-session-token-hash");
        otherSession.setCreatedAt(secondCreatedAt);

        PageResult<Session> sessions = new PageResult<>(
                List.of(currentSession, otherSession),
                2,
                1,
                1,
                10
        );

        given(sessionRepository.findAllByAccountIdAndNotRevoked(account.getId(), paging))
                .willReturn(sessions);

        given(secureHasher.hash(currentSessionToken))
                .willReturn(currentSessionTokenHash);

        PageResult<SessionOutput> result = service.execute(account, currentSessionToken, paging);

        assertEquals(2, result.content().size());
        assertEquals(2, result.totalItems());
        assertEquals(1, result.totalPages());
        assertEquals(1, result.page());
        assertEquals(10, result.size());

        SessionOutput currentResult = result.content().get(0);
        assertEquals(currentSession.getId(), currentResult.id());
        assertEquals(DeviceType.DESKTOP, currentResult.type());
        assertEquals(firstLastSeenAt, currentResult.lastSeenAt());
        assertEquals("Sao Paulo, Brazil", currentResult.location());
        assertTrue(currentResult.isCurrent());
        assertEquals(firstCreatedAt, currentResult.createdAt());

        SessionOutput otherResult = result.content().get(1);
        assertEquals(otherSession.getId(), otherResult.id());
        assertEquals(DeviceType.MOBILE, otherResult.type());
        assertFalse(otherResult.isCurrent());

        then(sessionRepository).should()
                .findAllByAccountIdAndNotRevoked(account.getId(), paging);

        then(secureHasher).should().hash(currentSessionToken);
    }

    @Test
    @DisplayName("Should reject the request when the current session token is blank")
    void givenBlankCurrentSessionToken_whenGetSessions_thenSessionNotFoundExceptionIsThrown() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        PageQuery paging = new PageQuery(1, 10);

        assertThrows(
                SessionNotFoundException.class,
                () -> service.execute(account, "   ", paging)
        );

        then(sessionRepository).shouldHaveNoInteractions();
        then(secureHasher).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should return an empty page when there are no active sessions")
    void givenNoActiveSessions_whenGetSessions_thenEmptyPageIsReturned() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        String currentSessionToken = "current-session-token";
        PageQuery paging = new PageQuery(2, 5);
        PageResult<Session> sessions = new PageResult<>(List.of(), 0, 0, 2, 5);

        given(sessionRepository.findAllByAccountIdAndNotRevoked(account.getId(), paging))
                .willReturn(sessions);

        given(secureHasher.hash(currentSessionToken))
                .willReturn("current-session-token-hash");

        PageResult<SessionOutput> result = service.execute(account, currentSessionToken, paging);

        assertEquals(List.of(), result.content());
        assertEquals(0, result.totalItems());
        assertEquals(0, result.totalPages());
        assertEquals(2, result.page());
        assertEquals(5, result.size());
        then(sessionRepository).should()
                .findAllByAccountIdAndNotRevoked(account.getId(), paging);
        then(secureHasher).should().hash(currentSessionToken);
    }
}
