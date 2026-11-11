package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.ChangePasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendPasswordChangedUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ChangePasswordInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.PasswordSameAsCurrentException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ChangePasswordServiceTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private RevokeAllSessionsUseCase revokeAllSessionsUseCase;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SendPasswordChangedUseCase sendPasswordChangedUseCase;

    @InjectMocks
    private ChangePasswordService service;

    @Test
    @DisplayName("Should change the password, save the account and sign out all sessions")
    void givenValidCurrentAndNewPasswords_whenChangePassword_thenAccountIsUpdatedAndSessionsAreRevoked() {
        Account account = new Account();
        account.setPasswordHash("current-password-hash");
        ChangePasswordInput command = new ChangePasswordInput("current-password", "new-password", "new-password");

        given(passwordEncoder.matches(command.currentPassword(), account.getPasswordHash()))
                .willReturn(true);
        given(passwordEncoder.matches(command.newPassword(), account.getPasswordHash()))
                .willReturn(false);
        given(passwordEncoder.hash(command.newPassword()))
                .willReturn("new-password-hash");

        service.execute(account, command);

        assertEquals("new-password-hash", account.getPasswordHash());
        then(accountRepository).should().save(account);
        then(revokeAllSessionsUseCase).should().execute(account);
    }

    @Test
    @DisplayName("Should reject the password change when the current password is invalid")
    void givenInvalidCurrentPassword_whenChangePassword_thenInvalidCredentialsExceptionIsThrown() {
        Account account = new Account();
        account.setPasswordHash("current-password-hash");
        ChangePasswordInput command = new ChangePasswordInput("wrong-password", "new-password", "new-password");

        given(passwordEncoder.matches(command.currentPassword(), account.getPasswordHash()))
                .willReturn(false);

        assertThrows(ChangePasswordInvalidCredentialsException.class, () -> service.execute(account, command));

        then(passwordEncoder).should().matches(command.currentPassword(), account.getPasswordHash());
        then(passwordEncoder).should(never()).matches(command.newPassword(), account.getPasswordHash());
        then(accountRepository).should(never()).save(account);
        then(revokeAllSessionsUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject the password change when the new password matches the current password")
    void givenNewPasswordEqualToCurrentPassword_whenChangePassword_thenSamePasswordExceptionIsThrown() {
        Account account = new Account();
        account.setPasswordHash("current-password-hash");
        ChangePasswordInput command = new ChangePasswordInput("current-password", "same-password", "same-password");

        given(passwordEncoder.matches(command.currentPassword(), account.getPasswordHash()))
                .willReturn(true);
        given(passwordEncoder.matches(command.newPassword(), account.getPasswordHash()))
                .willReturn(true);

        assertThrows(PasswordSameAsCurrentException.class, () -> service.execute(account, command));

        then(passwordEncoder).should(never()).hash(command.newPassword());
        then(accountRepository).should(never()).save(account);
        then(revokeAllSessionsUseCase).shouldHaveNoInteractions();
    }
}
