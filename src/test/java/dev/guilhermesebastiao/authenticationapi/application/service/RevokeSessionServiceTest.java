package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.*;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RefreshTokenNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.SessionNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RevokeSessionServiceTest {
    @Mock SessionRepositoryPort sessions;
    @Mock RefreshRepositoryPort refreshes;
    @Mock SecureHasherPort hasher;

    @Test void revokesSessionAndRefreshByToken() {
        var service = new RevokeSessionService(sessions, refreshes, hasher);
        var account = new Account();
        var session = new Session();
        session.setId(UUID.randomUUID());
        var refresh = new Refresh();
        when(hasher.hash("token")).thenReturn("hash");
        when(sessions.findBySessionTokenHash("hash")).thenReturn(Optional.of(session));
        when(refreshes.findByRefreshBySessionId(session.getId())).thenReturn(Optional.of(refresh));

        service.execute(account, "token");

        assertNotNull(session.getRevokedAt());
        assertSame(session.getRevokedAt(), refresh.getRevokedAt());
        verify(refreshes).save(refresh);
        verify(sessions).save(session);
    }

    @Test void throwsWhenSessionDoesNotExist() {
        var service = new RevokeSessionService(sessions, refreshes, hasher);
        when(sessions.findById(any())).thenReturn(Optional.empty());
        assertThrows(SessionNotFoundException.class, () -> service.execute(new Account(), UUID.randomUUID()));
        verifyNoInteractions(refreshes);
    }

    @Test void throwsWhenRefreshDoesNotExist() {
        var service = new RevokeSessionService(sessions, refreshes, hasher);
        var session = new Session();
        session.setId(UUID.randomUUID());
        when(sessions.findById(session.getId())).thenReturn(Optional.of(session));
        when(refreshes.findByRefreshBySessionId(session.getId())).thenReturn(Optional.empty());
        assertThrows(RefreshTokenNotFoundException.class, () -> service.execute(new Account(), session.getId()));
        verify(sessions, never()).save(session);
    }
}
