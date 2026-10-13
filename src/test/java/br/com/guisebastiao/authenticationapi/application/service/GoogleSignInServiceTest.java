package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.command.GoogleSignInCommand;
import br.com.guisebastiao.authenticationapi.application.port.in.CreateAccountActivationUseCase;
import br.com.guisebastiao.authenticationapi.application.port.in.CreateRefreshUseCase;
import br.com.guisebastiao.authenticationapi.application.port.in.CreateSessionUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.GoogleAuthorizationPort;
import br.com.guisebastiao.authenticationapi.application.port.out.JwtTokenPort;
import br.com.guisebastiao.authenticationapi.application.result.AccountActivationResult;
import br.com.guisebastiao.authenticationapi.application.result.AuthResult;
import br.com.guisebastiao.authenticationapi.application.result.CreateRefreshResult;
import br.com.guisebastiao.authenticationapi.application.result.CreateSessionResult;
import br.com.guisebastiao.authenticationapi.application.result.GoogleAuthorizationResult;
import br.com.guisebastiao.authenticationapi.domain.enums.AccountStatus;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountDisabledException;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountNotActivatedException;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountNotFoundException;
import br.com.guisebastiao.authenticationapi.domain.model.Account;
import br.com.guisebastiao.authenticationapi.domain.model.Refresh;
import br.com.guisebastiao.authenticationapi.domain.model.Session;
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
        GoogleSignInCommand command = new GoogleSignInCommand("google-credential");
        String userAgent = "Mozilla/5.0";
        String ipAddress = "192.168.0.10";
        GoogleAuthorizationResult googleResult = new GoogleAuthorizationResult("user@example.com");
        Account account = new Account();
        account.setEmail(googleResult.email());
        account.setStatus(AccountStatus.ACTIVATED);
        Session session = new Session();
        CreateSessionResult sessionResult = new CreateSessionResult("session-token", session);
        CreateRefreshResult refreshResult = new CreateRefreshResult("refresh-token", new Refresh());

        given(googleAuthorization.authorize(command.code()))
                .willReturn(googleResult);

        given(accountRepository.findByEmail(googleResult.email()))
                .willReturn(java.util.Optional.of(account));

        given(createSessionUseCase.execute(account, userAgent, ipAddress))
                .willReturn(sessionResult);

        given(createRefreshUseCase.execute(session))
                .willReturn(refreshResult);

        given(jwtToken.generate(account, sessionResult.sessionToken()))
                .willReturn("access-token");

        AuthResult result = service.execute(command, userAgent, ipAddress);

        assertEquals("access-token", result.accessToken());
        assertEquals(refreshResult.refreshToken(), result.refreshToken());
        assertEquals(sessionResult.sessionToken(), result.sessionToken());

        then(googleAuthorization).should().authorize(command.code());
        then(accountRepository).should().findByEmail(googleResult.email());
        then(createSessionUseCase).should().execute(account, userAgent, ipAddress);
        then(createRefreshUseCase).should().execute(session);
        then(jwtToken).should().generate(account, sessionResult.sessionToken());
        then(createAccountActivationUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject Google sign in when the account does not exist")
    void givenGoogleCredentialForUnknownAccount_whenGoogleSignIn_thenAccountNotFoundExceptionIsThrown() {
        GoogleSignInCommand command = new GoogleSignInCommand("google-credential");
        GoogleAuthorizationResult googleResult = new GoogleAuthorizationResult("unknown@example.com");

        given(googleAuthorization.authorize(command.code()))
                .willReturn(googleResult);

        given(accountRepository.findByEmail(googleResult.email()))
                .willReturn(java.util.Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> service.execute(command, "Mozilla/5.0", "192.168.0.10")
        );

        then(googleAuthorization).should().authorize(command.code());
        then(accountRepository).should().findByEmail(googleResult.email());
        then(createAccountActivationUseCase).shouldHaveNoInteractions();
        then(createSessionUseCase).shouldHaveNoInteractions();
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(jwtToken).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should request account activation and reject Google sign in for a pending account")
    void givenPendingAccount_whenGoogleSignIn_thenAccountNotActivatedExceptionIsThrown() {
        GoogleSignInCommand command = new GoogleSignInCommand("google-credential");
        GoogleAuthorizationResult googleResult = new GoogleAuthorizationResult("pending@example.com");
        Account account = new Account();
        account.setEmail(googleResult.email());
        account.setStatus(AccountStatus.PENDING);
        AccountActivationResult activationResult = new AccountActivationResult(
                googleResult.email(),
                "activation-token",
                Instant.parse("2026-10-04T12:15:00Z"),
                Instant.parse("2026-10-04T12:01:00Z")
        );

        given(googleAuthorization.authorize(command.code()))
                .willReturn(googleResult);

        given(accountRepository.findByEmail(googleResult.email()))
                .willReturn(java.util.Optional.of(account));

        given(createAccountActivationUseCase.execute(account))
                .willReturn(activationResult);

        AccountNotActivatedException exception = assertThrows(
                AccountNotActivatedException.class,
                () -> service.execute(command, "Mozilla/5.0", "192.168.0.10")
        );

        assertSame(activationResult, exception.getDetails());
        then(createAccountActivationUseCase).should().execute(account);
        then(createSessionUseCase).shouldHaveNoInteractions();
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(jwtToken).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject Google sign in for a disabled account")
    void givenDisabledAccount_whenGoogleSignIn_thenAccountDisabledExceptionIsThrown() {
        GoogleSignInCommand command = new GoogleSignInCommand("google-credential");
        GoogleAuthorizationResult googleResult = new GoogleAuthorizationResult("disabled@example.com");
        Account account = new Account();
        account.setEmail(googleResult.email());
        account.setStatus(AccountStatus.DISABLED);

        given(googleAuthorization.authorize(command.code()))
                .willReturn(googleResult);

        given(accountRepository.findByEmail(googleResult.email()))
                .willReturn(java.util.Optional.of(account));

        assertThrows(
                AccountDisabledException.class,
                () -> service.execute(command, "Mozilla/5.0", "192.168.0.10")
        );

        then(createAccountActivationUseCase).shouldHaveNoInteractions();
        then(createSessionUseCase).shouldHaveNoInteractions();
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(jwtToken).shouldHaveNoInteractions();
    }
}
