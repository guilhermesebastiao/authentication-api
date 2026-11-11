package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.DisableAccountInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountDisabledUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.DisableAccountInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class DisableAccountServiceTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private RevokeAllSessionsUseCase revokeAllSessionsUseCase;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SendAccountDisabledUseCase sendAccountDisabledUseCase;

    @InjectMocks
    private DisableAccountService service;

    @Test
    @DisplayName("Should disable the account, save it and sign out all sessions")
    void givenValidPassword_whenDisableAccount_thenAccountIsDisabledAndSessionsAreSignedOut() {
        Account account = new Account();
        account.setPasswordHash("password-hash");
        DisableAccountInput command = new DisableAccountInput("current-password");
        Instant beforeExecution = Instant.now();

        given(passwordEncoder.matches(command.password(), account.getPasswordHash()))
                .willReturn(true);

        service.execute(account, command);
        Instant afterExecution = Instant.now();

        assertNotNull(account.getDisabledAt());
        assertFalse(account.getDisabledAt().isBefore(beforeExecution));
        assertFalse(account.getDisabledAt().isAfter(afterExecution));

        then(passwordEncoder).should()
                .matches(command.password(), account.getPasswordHash());

        then(accountRepository).should().save(account);
        then(revokeAllSessionsUseCase).should().execute(account);
    }

    @Test
    @DisplayName("Should reject account disabling when the password is invalid")
    void givenInvalidPassword_whenDisableAccount_thenInvalidCredentialsExceptionIsThrown() {
        Account account = new Account();
        account.setPasswordHash("password-hash");
        DisableAccountInput command = new DisableAccountInput("wrong-password");

        given(passwordEncoder.matches(command.password(), account.getPasswordHash()))
                .willReturn(false);

        assertThrows(DisableAccountInvalidCredentialsException.class, () -> service.execute(account, command));

        assertNull(account.getDisabledAt());

        then(passwordEncoder).should()
                .matches(command.password(), account.getPasswordHash());

        then(accountRepository).should(never()).save(account);
        then(revokeAllSessionsUseCase).shouldHaveNoInteractions();
    }
}
