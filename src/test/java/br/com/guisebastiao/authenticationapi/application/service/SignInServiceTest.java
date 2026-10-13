package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.command.SignInCommand;
import br.com.guisebastiao.authenticationapi.application.port.in.CreateAccountActivationUseCase;
import br.com.guisebastiao.authenticationapi.application.port.in.CreateRefreshUseCase;
import br.com.guisebastiao.authenticationapi.application.port.in.CreateSessionUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.AuthenticationPort;
import br.com.guisebastiao.authenticationapi.application.port.out.JwtTokenPort;
import br.com.guisebastiao.authenticationapi.application.result.AccountActivationResult;
import br.com.guisebastiao.authenticationapi.application.result.AuthResult;
import br.com.guisebastiao.authenticationapi.application.result.CreateRefreshResult;
import br.com.guisebastiao.authenticationapi.application.result.CreateSessionResult;
import br.com.guisebastiao.authenticationapi.domain.enums.AccountStatus;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountInvalidCredentialsException;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountNotActivatedException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SignInServiceTest {

    @Mock
    private CreateAccountActivationUseCase createAccountActivationUseCase;

    @Mock
    private CreateSessionUseCase createSessionUseCase;

    @Mock
    private CreateRefreshUseCase createRefreshUseCase;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private AuthenticationPort authenticationPort;

    @Mock
    private JwtTokenPort jwtToken;

    @InjectMocks
    private SignInService service;

    @Test
    @DisplayName("Should sign in an activated account and return authentication tokens")
    void givenActivatedAccountAndValidCredentials_whenSignIn_thenAuthenticationTokensAreReturned() {
        SignInCommand command = new SignInCommand("user@example.com", "plain-password");
        String userAgent = "Mozilla/5.0";
        String ipAddress = "192.168.0.10";

        Account account = new Account();
        account.setEmail(command.email());
        account.setStatus(AccountStatus.ACTIVATED);

        Session session = new Session();
        CreateSessionResult sessionResult = new CreateSessionResult("session-token", session);
        CreateRefreshResult refreshResult = new CreateRefreshResult("refresh-token", new Refresh());
        String accessToken = "access-token";

        given(accountRepository.findByEmail(command.email()))
                .willReturn(Optional.of(account));

        given(createSessionUseCase.execute(account, userAgent, ipAddress))
                .willReturn(sessionResult);

        given(createRefreshUseCase.execute(session))
                .willReturn(refreshResult);

        given(jwtToken.generate(account, sessionResult.sessionToken()))
                .willReturn(accessToken);

        AuthResult result = service.execute(command, userAgent, ipAddress);

        assertEquals(accessToken, result.accessToken());
        assertEquals(refreshResult.refreshToken(), result.refreshToken());
        assertEquals(sessionResult.sessionToken(), result.sessionToken());

        then(accountRepository).should().findByEmail(command.email());

        then(authenticationPort).should()
                .authenticate(command.email(), command.password());

        then(createSessionUseCase).should()
                .execute(account, userAgent, ipAddress);

        then(createRefreshUseCase).should().execute(session);
        then(jwtToken).should().generate(account, sessionResult.sessionToken());
        then(createAccountActivationUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject sign in when the account does not exist")
    void givenUnknownAccount_whenSignIn_thenInvalidCredentialsExceptionIsThrown() {
        SignInCommand command = new SignInCommand("unknown@example.com", "plain-password");

        given(accountRepository.findByEmail(command.email()))
                .willReturn(Optional.empty());

        assertThrows(AccountInvalidCredentialsException.class, () ->
                service.execute(command, "Mozilla/5.0", "192.168.0.10")
        );

        then(accountRepository).should().findByEmail(command.email());
        then(createAccountActivationUseCase).shouldHaveNoInteractions();
        then(authenticationPort).shouldHaveNoInteractions();
        then(createSessionUseCase).shouldHaveNoInteractions();
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(jwtToken).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should request a new activation and reject sign in for a pending account")
    void givenPendingAccount_whenSignIn_thenAccountNotActivatedExceptionIsThrown() {
        SignInCommand command = new SignInCommand("pending@example.com", "plain-password");
        Account account = new Account();
        account.setEmail(command.email());
        account.setStatus(AccountStatus.PENDING);

        AccountActivationResult activationResult = new AccountActivationResult(
                command.email(),
                "activation-token",
                Instant.parse("2026-10-04T12:15:00Z"),
                Instant.parse("2026-10-04T12:01:00Z")
        );

        given(accountRepository.findByEmail(command.email()))
                .willReturn(Optional.of(account));

        given(createAccountActivationUseCase.execute(account))
                .willReturn(activationResult);

        AccountNotActivatedException exception = assertThrows(
                AccountNotActivatedException.class,
                () -> service.execute(command, "Mozilla/5.0", "192.168.0.10")
        );

        assertSame(activationResult, exception.getDetails());

        then(accountRepository).should().findByEmail(command.email());
        then(createAccountActivationUseCase).should().execute(account);
        then(authenticationPort).shouldHaveNoInteractions();
        then(createSessionUseCase).shouldHaveNoInteractions();
        then(createRefreshUseCase).shouldHaveNoInteractions();
        then(jwtToken).shouldHaveNoInteractions();
    }
}
