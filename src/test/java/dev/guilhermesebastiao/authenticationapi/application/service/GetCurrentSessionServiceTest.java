package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetDeviceNameUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotBelongToAccountException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GetCurrentSessionServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepository;

    @Mock
    private SecureHasherPort secureHasher;

    @Mock
    private GetDeviceNameUseCase getDeviceNameUseCase;

    @InjectMocks
    private GetCurrentSessionService service;

    @Test
    @DisplayName("Should return the current session when it belongs to the account")
    void givenSessionBelongingToAccount_whenGetCurrentSession_thenSessionResultIsReturned() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        String currentSessionToken = "current-session-token";
        String currentSessionTokenHash = "current-session-token-hash";
        Instant lastSeenAt = Instant.parse("2026-10-04T10:00:00Z");
        Instant createdAt = Instant.parse("2026-10-01T10:00:00Z");

        Session session = new Session();
        session.setId(UUID.randomUUID());
        session.setAccount(account);
        session.setSessionTokenHash(currentSessionTokenHash);
        session.setType(DeviceType.DESKTOP);
        session.setLastSeenAt(lastSeenAt);
        session.setLocation("Sao Paulo, Brazil");
        session.setCreatedAt(createdAt);

        given(secureHasher.hash(currentSessionToken))
                .willReturn(currentSessionTokenHash);

        given(sessionRepository.findBySessionTokenHash(currentSessionTokenHash))
                .willReturn(Optional.of(session));
        given(getDeviceNameUseCase.execute(null)).willReturn("Dispositivo desconhecido");

        SessionOutput result = service.execute(account, currentSessionToken);

        assertEquals(session.getId(), result.id());
        assertEquals(DeviceType.DESKTOP, result.type());
        assertEquals(lastSeenAt, result.lastSeenAt());
        assertEquals("Sao Paulo, Brazil", result.location());
        assertTrue(result.isCurrent());
        assertEquals(createdAt, result.createdAt());

        then(secureHasher).should().hash(currentSessionToken);
        then(sessionRepository).should().findBySessionTokenHash(currentSessionTokenHash);
    }

    @Test
    @DisplayName("Should reject the request when the current session is not found")
    void givenUnknownCurrentSessionToken_whenGetCurrentSession_thenSessionNotFoundExceptionIsThrown() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        String currentSessionToken = "unknown-session-token";
        String currentSessionTokenHash = "unknown-session-token-hash";

        given(secureHasher.hash(currentSessionToken))
                .willReturn(currentSessionTokenHash);

        given(sessionRepository.findBySessionTokenHash(currentSessionTokenHash))
                .willReturn(Optional.empty());

        assertThrows(
                SessionNotFoundException.class,
                () -> service.execute(account, currentSessionToken)
        );

        then(secureHasher).should().hash(currentSessionToken);
        then(sessionRepository).should().findBySessionTokenHash(currentSessionTokenHash);
    }

    @Test
    @DisplayName("Should reject the session when it belongs to another account")
    void givenSessionBelongingToAnotherAccount_whenGetCurrentSession_thenSessionNotBelongToAccountExceptionIsThrown() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        Account anotherAccount = new Account();
        anotherAccount.setId(UUID.randomUUID());
        String currentSessionToken = "current-session-token";
        String currentSessionTokenHash = "current-session-token-hash";
        Session session = new Session();
        session.setAccount(anotherAccount);

        given(secureHasher.hash(currentSessionToken))
                .willReturn(currentSessionTokenHash);

        given(sessionRepository.findBySessionTokenHash(currentSessionTokenHash))
                .willReturn(Optional.of(session));

        assertThrows(
                SessionNotBelongToAccountException.class,
                () -> service.execute(account, currentSessionToken)
        );

        then(secureHasher).should().hash(currentSessionToken);
        then(sessionRepository).should().findBySessionTokenHash(currentSessionTokenHash);
    }
}
