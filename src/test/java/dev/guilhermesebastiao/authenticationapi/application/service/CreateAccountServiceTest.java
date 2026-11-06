package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.CreateAccountInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RoleRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyExistsException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RoleNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CreateAccountServiceTest {

    @Mock
    private CreateAccountActivationUseCase createAccountActivationUseCase;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private RoleRepositoryPort roleRepository;

    @InjectMocks
    private CreateAccountService service;

    @Test
    @DisplayName("Should create a pending account with the default role and request activation")
    void givenNewAccountData_whenCreateAccount_thenAccountIsSavedAndActivationIsCreated() {
        CreateAccountInput command = new CreateAccountInput("user@example.com", "plain-password");
        String passwordHash = "hashed-password";
        Role defaultRole = new Role();

        AccountActivationOutput expectedResult = new AccountActivationOutput(
                command.email(),
                "activation-token",
                Instant.parse("2026-10-04T12:15:00Z"),
                Instant.parse("2026-10-04T12:01:00Z")
        );

        given(accountRepository.findByEmail(command.email()))
                .willReturn(Optional.empty());

        given(passwordEncoder.hash(command.password()))
                .willReturn(passwordHash);

        given(roleRepository.findRoleByName("ROLE_USER"))
                .willReturn(Optional.of(defaultRole));

        given(accountRepository.save(any(Account.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        given(createAccountActivationUseCase.execute(any(Account.class)))
                .willReturn(expectedResult);

        AccountActivationOutput actualResult = service.execute(command);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        then(accountRepository).should().save(accountCaptor.capture());
        Account savedAccount = accountCaptor.getValue();

        assertEquals(command.email(), savedAccount.getEmail());
        assertEquals(passwordHash, savedAccount.getPasswordHash());
        assertEquals(AccountStatus.PENDING, savedAccount.getStatus());
        assertEquals(Set.of(defaultRole), savedAccount.getRoles());
        assertSame(expectedResult, actualResult);

        then(createAccountActivationUseCase).should().execute(savedAccount);
        then(passwordEncoder).should().hash(command.password());
        then(roleRepository).should().findRoleByName("ROLE_USER");
        then(accountRepository).should(times(1)).findByEmail(command.email());
    }

    @Test
    @DisplayName("Should reject account creation when the email is already registered")
    void givenExistingAccount_whenCreateAccount_thenAccountAlreadyExistsExceptionIsThrown() {
        CreateAccountInput command = new CreateAccountInput("existing@example.com", "plain-password");

        Account existingAccount = new Account();

        given(accountRepository.findByEmail(command.email()))
                .willReturn(Optional.of(existingAccount));

        assertThrows(AccountAlreadyExistsException.class, () -> service.execute(command));

        then(accountRepository).should(times(1)).findByEmail(command.email());
        then(accountRepository).should(never()).save(any(Account.class));
        then(passwordEncoder).shouldHaveNoInteractions();
        then(roleRepository).shouldHaveNoInteractions();
        then(createAccountActivationUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject account creation when the default role does not exist")
    void givenMissingDefaultRole_whenCreateAccount_thenRoleNotFoundExceptionIsThrown() {
        CreateAccountInput command = new CreateAccountInput("user@example.com", "plain-password");

        given(accountRepository.findByEmail(command.email()))
                .willReturn(Optional.empty());

        given(passwordEncoder.hash(command.password()))
                .willReturn("hashed-password");

        given(roleRepository.findRoleByName("ROLE_USER"))
                .willReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> service.execute(command));

        then(accountRepository).should(times(1)).findByEmail(command.email());
        then(roleRepository).should().findRoleByName("ROLE_USER");
        then(accountRepository).should(never()).save(any(Account.class));
        then(createAccountActivationUseCase).shouldHaveNoInteractions();
    }
}
