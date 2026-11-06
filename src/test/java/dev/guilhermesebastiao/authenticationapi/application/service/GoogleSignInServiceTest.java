package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateRefreshUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.GoogleAuthorizationPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.JwtTokenPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AuthOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateRefreshOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateSessionOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.GoogleAuthorizationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountDisabledException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountNotActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GoogleSignInServiceTest {

    @Mock
    private CreateAccountActivationUseCase createAccountActivationUseCase;

    @Mock
    private GoogleAuthorizationPort googleAuthorization;

    @Mock
    private CreateSessionUseCase createSessionUseCase;

    @Mock
    private CreateRefreshUseCase createRefreshUseCase;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private JwtTokenPort jwtToken;

    @InjectMocks
    private GoogleSignInService service;

    @Test
    @DisplayName("Should sign in an activated account using Google authorization")
    void givenActivatedAccountAndValidGoogleCredential_whenGoogleSignIn_thenAuthenticationTokensAreReturned() {
        String code = "google-credential";
        String userAgent = "Mozilla/5.0";
        String ipAddress = "192.168.0.10";
        GoogleAuthorizationOutput googleResult = new GoogleAuthorizationOutput("user@example.com");
        Account account = new Account();
        account.setEmail(googleResult.email());
        account.setStatus(AccountStatus.ACTIVATED);
        Session session = new Session();
        CreateSessionOutput sessionResult = new CreateSessionOutput("session-token", session);
        CreateRefreshOutput refreshResult = new CreateRefreshOutput("refresh-token", new Refresh());

        given(googleAuthorization.authorize(code))
                .willReturn(googleResult);

        given(accountRepository.findByEmail(googleResult.email()))
                .willReturn(java.util.Optional.of(account));

        given(createSessionUseCase.execute(account, userAgent, ipAddress))
                .willReturn(sessionResult);

        given(createRefreshUseCase.execute(session))
                .willReturn(refreshResult);

        given(jwtToken.generate(account, sessionResult.sessionToken()))
                .willReturn("access-token");

        AuthOutput result = service.execute(code, userAgent, ipAddress);

        assertEquals("access-token", result.accessToken());
        assertEquals(refreshResult.refreshToken(), result.refreshToken());
        assertEquals(sessionResult.sessionToken(), result.sessionToken());

        then(googleAuthorization).should().authorize(code);
        then(accountRepository).should().findByEmail(googleResult.email());
        then(createSessionUseCase).should().execute(account, userAgent, ipAddress);
        then(createRefreshUseCase).should().execute(session);
        then(jwtToken).should().generate(account, sessionResult.sessionToken());
        then(createAccountActivationUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject Google sign in when the account does not exist")
    void givenGoogleCredentialForUnknownAccount_whenGoogleSignIn_thenAccountNotFoundExceptionIsThrown() {
        String code = "google-credential";
        GoogleAuthorizationOutput googleResult = new GoogleAuthorizationOutput("unknown@example.com");

        given(googleAuthorization.authorize(code))
                .willReturn(googleResult);

        given(accountRepository.findByEmail(googleResult.email()))
                .willReturn(java.util.Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> service.execute(code, "Mozilla/5.0", "192.168.0.10")
        );

        then(googleAuthorization).should().authorize(code);
        then(accountRepository).should().findByEmail(googleResult.email());
        then(createAccountActivationUseCase).shouldHaveNoInteractions();
        then(createSessionUseCase).shouldHaveNoInteractions();
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(jwtToken).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should request account activation and reject Google sign in for a pending account")
    void givenPendingAccount_whenGoogleSignIn_thenAccountNotActivatedExceptionIsThrown() {
        String code = "google-credential";
        GoogleAuthorizationOutput googleResult = new GoogleAuthorizationOutput("pending@example.com");
        Account account = new Account();
        account.setEmail(googleResult.email());
        account.setStatus(AccountStatus.PENDING);
        AccountActivationOutput activationResult = new AccountActivationOutput(
                googleResult.email(),
                "activation-token",
                Instant.parse("2026-10-04T12:15:00Z"),
                Instant.parse("2026-10-04T12:01:00Z")
        );

        given(googleAuthorization.authorize(code))
                .willReturn(googleResult);

        given(accountRepository.findByEmail(googleResult.email()))
                .willReturn(java.util.Optional.of(account));

        given(createAccountActivationUseCase.execute(account))
                .willReturn(activationResult);

        AccountNotActivatedException exception = assertThrows(
                AccountNotActivatedException.class,
                () -> service.execute(code, "Mozilla/5.0", "192.168.0.10")
        );

        assertEquals("The account has not been activated.", exception.getDetail());
        then(createAccountActivationUseCase).should().execute(account);
        then(createSessionUseCase).shouldHaveNoInteractions();
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(jwtToken).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject Google sign in for a disabled account")
    void givenDisabledAccount_whenGoogleSignIn_thenAccountDisabledExceptionIsThrown() {
        String code = "google-credential";
        GoogleAuthorizationOutput googleResult = new GoogleAuthorizationOutput("disabled@example.com");
        Account account = new Account();
        account.setEmail(googleResult.email());
        account.setStatus(AccountStatus.DISABLED);

        given(googleAuthorization.authorize(code))
                .willReturn(googleResult);

        given(accountRepository.findByEmail(googleResult.email()))
                .willReturn(java.util.Optional.of(account));

        assertThrows(
                AccountDisabledException.class,
                () -> service.execute(code, "Mozilla/5.0", "192.168.0.10")
        );

        then(createAccountActivationUseCase).shouldHaveNoInteractions();
        then(createSessionUseCase).shouldHaveNoInteractions();
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(jwtToken).shouldHaveNoInteractions();
    }
}
