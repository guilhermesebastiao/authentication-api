package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.RefreshTokenInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateRefreshUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ValidateSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.JwtTokenPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AuthOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateRefreshOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.JwtValidationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.JwtValidationStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RefreshTokenInvalidException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RefreshTokenNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RefreshTokenRevokedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.UnauthorizedException;
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
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private ValidateSessionUseCase validateSessionUseCase;

    @Mock
    private CreateRefreshUseCase createRefreshUseCase;

    @Mock
    private RefreshRepositoryPort refreshRepository;

    @Mock
    private SessionRepositoryPort sessionRepository;

    @Mock
    private SecureHasherPort secureHasher;

    @Mock
    private JwtTokenPort jwtToken;

    @InjectMocks
    private RefreshTokenService service;

    @Test
    @DisplayName("Should return the existing tokens when the access token is valid")
    void givenValidAccessToken_whenRefreshTokens_thenExistingTokensAreReturned() {
        RefreshTokenInput command = new RefreshTokenInput("access-token", "refresh-token", "session-token");

        Session session = new Session();

        given(validateSessionUseCase.execute(command.sessionToken()))
                .willReturn(session);

        given(jwtToken.validate(command.accessToken(), command.sessionToken()))
                .willReturn(JwtValidationOutput.valid(UUID.randomUUID()));

        AuthOutput result = service.execute(command);

        assertEquals(command.accessToken(), result.accessToken());
        assertEquals(command.refreshToken(), result.refreshToken());
        assertEquals(command.sessionToken(), result.sessionToken());

        then(validateSessionUseCase).should().execute(command.sessionToken());
        then(jwtToken).should().validate(command.accessToken(), command.sessionToken());
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(refreshRepository).shouldHaveNoInteractions();
        then(sessionRepository).shouldHaveNoInteractions();
        then(secureHasher).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject the request when the access token is invalid")
    void givenInvalidAccessToken_whenRefreshTokens_thenUnauthorizedExceptionIsThrown() {
        RefreshTokenInput command = new RefreshTokenInput("invalid-access-token", "refresh-token", "session-token");

        Session session = new Session();

        given(validateSessionUseCase.execute(command.sessionToken()))
                .willReturn(session);

        given(jwtToken.validate(command.accessToken(), command.sessionToken()))
                .willReturn(JwtValidationOutput.invalid(JwtValidationStatus.INVALID_SIGNATURE));

        assertThrows(UnauthorizedException.class, () -> service.execute(command));

        then(validateSessionUseCase).should().execute(command.sessionToken());
        then(jwtToken).should().validate(command.accessToken(), command.sessionToken());
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(refreshRepository).shouldHaveNoInteractions();
        then(sessionRepository).shouldHaveNoInteractions();
        then(secureHasher).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should rotate the refresh token and renew the session when the access token is expired")
    void givenExpiredAccessTokenAndValidRefreshToken_whenRefreshTokens_thenTokensAndSessionAreRenewed() {
        RefreshTokenInput command = new RefreshTokenInput("expired-access-token", "refresh-token", "session-token");

        Account account = new Account();
        Session session = new Session();
        session.setId(UUID.randomUUID());
        session.setAccount(account);

        Refresh previousRefresh = new Refresh();
        previousRefresh.setSession(session);
        Refresh replacementRefresh = new Refresh();
        CreateRefreshOutput refreshResult = new CreateRefreshOutput("new-refresh-token", replacementRefresh);

        Instant beforeExecution = Instant.now();

        given(validateSessionUseCase.execute(command.sessionToken()))
                .willReturn(session);

        given(jwtToken.validate(command.accessToken(), command.sessionToken()))
                .willReturn(JwtValidationOutput.invalid(JwtValidationStatus.EXPIRED));

        given(secureHasher.hash(command.refreshToken()))
                .willReturn("refresh-token-hash");

        given(refreshRepository.findByRefreshTokenHash("refresh-token-hash"))
                .willReturn(java.util.Optional.of(previousRefresh));

        given(createRefreshUseCase.execute(session))
                .willReturn(refreshResult);

        given(jwtToken.generate(account, command.sessionToken()))
                .willReturn("new-access-token");

        AuthOutput result = service.execute(command);
        Instant afterExecution = Instant.now();

        assertEquals("new-access-token", result.accessToken());
        assertEquals(refreshResult.refreshToken(), result.refreshToken());
        assertEquals(command.sessionToken(), result.sessionToken());
        assertNotNull(previousRefresh.getRevokedAt());
        assertSame(replacementRefresh, previousRefresh.getReplacedBy());
        assertNotNull(session.getLastSeenAt());
        assertFalse(session.getLastSeenAt().isBefore(beforeExecution));
        assertFalse(session.getLastSeenAt().isAfter(afterExecution));
        assertEquals(7, ChronoUnit.DAYS.between(session.getLastSeenAt(), session.getExpiresAt()));

        then(validateSessionUseCase).should().execute(command.sessionToken());
        then(jwtToken).should().validate(command.accessToken(), command.sessionToken());
        then(secureHasher).should().hash(command.refreshToken());
        then(refreshRepository).should().findByRefreshTokenHash("refresh-token-hash");
        then(createRefreshUseCase).should().execute(session);
        then(refreshRepository).should().save(previousRefresh);
        then(sessionRepository).should().save(session);
        then(jwtToken).should().generate(account, command.sessionToken());
    }

    @Test
    @DisplayName("Should reject the request when the refresh token is not found")
    void givenExpiredAccessTokenAndUnknownRefreshToken_whenRefreshTokens_thenRefreshTokenNotFoundExceptionIsThrown() {
        RefreshTokenInput command = new RefreshTokenInput("expired-access-token", "unknown-refresh-token", "session-token");

        Session session = new Session();

        given(validateSessionUseCase.execute(command.sessionToken()))
                .willReturn(session);

        given(jwtToken.validate(command.accessToken(), command.sessionToken()))
                .willReturn(JwtValidationOutput.invalid(JwtValidationStatus.EXPIRED));

        given(secureHasher.hash(command.refreshToken()))
                .willReturn("unknown-refresh-token-hash");

        given(refreshRepository.findByRefreshTokenHash("unknown-refresh-token-hash"))
                .willReturn(java.util.Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> service.execute(command));

        then(validateSessionUseCase).should().execute(command.sessionToken());
        then(jwtToken).should().validate(command.accessToken(), command.sessionToken());
        then(secureHasher).should().hash(command.refreshToken());
        then(refreshRepository).should().findByRefreshTokenHash("unknown-refresh-token-hash");
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(refreshRepository).should(never()).save(any(Refresh.class));
        then(sessionRepository).shouldHaveNoInteractions();
        then(jwtToken).should(never()).generate(any(Account.class), anyString());
    }

    @Test
    @DisplayName("Should reject the request when the refresh token has been revoked")
    void givenExpiredAccessTokenAndRevokedRefreshToken_whenRefreshTokens_thenRefreshTokenRevokedExceptionIsThrown() {
        RefreshTokenInput command = new RefreshTokenInput("expired-access-token", "revoked-refresh-token", "session-token");

        Session session = new Session();
        Refresh revokedRefresh = new Refresh();
        revokedRefresh.setSession(session);
        revokedRefresh.setRevokedAt(Instant.now().minusSeconds(60));

        given(validateSessionUseCase.execute(command.sessionToken()))
                .willReturn(session);

        given(jwtToken.validate(command.accessToken(), command.sessionToken()))
                .willReturn(JwtValidationOutput.invalid(JwtValidationStatus.EXPIRED));

        given(secureHasher.hash(command.refreshToken()))
                .willReturn("revoked-refresh-token-hash");

        given(refreshRepository.findByRefreshTokenHash("revoked-refresh-token-hash"))
                .willReturn(java.util.Optional.of(revokedRefresh));

        assertThrows(RefreshTokenRevokedException.class, () -> service.execute(command));

        then(validateSessionUseCase).should().execute(command.sessionToken());
        then(jwtToken).should().validate(command.accessToken(), command.sessionToken());
        then(secureHasher).should().hash(command.refreshToken());
        then(refreshRepository).should().findByRefreshTokenHash("revoked-refresh-token-hash");
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(refreshRepository).should(never()).save(any(Refresh.class));
        then(sessionRepository).shouldHaveNoInteractions();
        then(jwtToken).should(never()).generate(any(Account.class), anyString());
    }

    @Test
    @DisplayName("Should reject the request when the refresh token belongs to another session")
    void givenExpiredAccessTokenAndRefreshTokenFromAnotherSession_whenRefreshTokens_thenRefreshTokenInvalidExceptionIsThrown() {
        RefreshTokenInput command = new RefreshTokenInput("expired-access-token", "another-session-refresh-token", "session-token");

        Session currentSession = new Session();
        currentSession.setId(UUID.randomUUID());
        Session refreshSession = new Session();
        refreshSession.setId(UUID.randomUUID());
        Refresh refresh = new Refresh();
        refresh.setSession(refreshSession);

        given(validateSessionUseCase.execute(command.sessionToken()))
                .willReturn(currentSession);

        given(jwtToken.validate(command.accessToken(), command.sessionToken()))
                .willReturn(JwtValidationOutput.invalid(JwtValidationStatus.EXPIRED));

        given(secureHasher.hash(command.refreshToken()))
                .willReturn("another-session-refresh-token-hash");

        given(refreshRepository.findByRefreshTokenHash("another-session-refresh-token-hash"))
                .willReturn(java.util.Optional.of(refresh));

        assertThrows(RefreshTokenInvalidException.class, () -> service.execute(command));

        then(validateSessionUseCase).should().execute(command.sessionToken());
        then(jwtToken).should().validate(command.accessToken(), command.sessionToken());
        then(secureHasher).should().hash(command.refreshToken());
        then(refreshRepository).should().findByRefreshTokenHash("another-session-refresh-token-hash");
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(refreshRepository).should(never()).save(any(Refresh.class));
        then(sessionRepository).shouldHaveNoInteractions();
        then(jwtToken).should(never()).generate(any(Account.class), anyString());
    }
}
