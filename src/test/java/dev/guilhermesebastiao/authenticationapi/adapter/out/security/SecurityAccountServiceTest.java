package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SecurityAccountServiceTest {

    @Mock
    private AccountRepositoryPort accountRepositoryPort;

    @InjectMocks
    private SecurityAccountService service;

    @Test
    @DisplayName("Should adapt an account found by email to SecurityAccount")
    void givenExistingEmail_whenLoadUserByUsername_thenReturnSecurityAccount() {
        String email = "user@example.com";
        Account account = new Account();
        account.setEmail(email);

        given(accountRepositoryPort.findByEmail(email)).willReturn(Optional.of(account));

        SecurityAccount result = service.loadUserByUsername(email);

        assertSame(account, result.account());
        assertEquals(email, result.getUsername());
        then(accountRepositoryPort).should().findByEmail(email);
    }

    @Test
    @DisplayName("Should reject an unknown email")
    void givenUnknownEmail_whenLoadUserByUsername_thenThrowUsernameNotFoundException() {
        String email = "unknown@example.com";
        given(accountRepositoryPort.findByEmail(email)).willReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername(email)
        );

        assertEquals("User not found", exception.getMessage());
        then(accountRepositoryPort).should().findByEmail(email);
    }

    @Test
    @DisplayName("Should adapt an account found by ID to SecurityAccount")
    void givenExistingUserId_whenLoadUserById_thenReturnSecurityAccount() {
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Account account = new Account();
        account.setId(userId);

        given(accountRepositoryPort.findById(userId)).willReturn(Optional.of(account));

        SecurityAccount result = service.loadUserById(userId);

        assertSame(account, result.account());
        then(accountRepositoryPort).should().findById(userId);
    }

    @Test
    @DisplayName("Should reject an unknown user ID")
    void givenUnknownUserId_whenLoadUserById_thenThrowUsernameNotFoundException() {
        UUID userId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        given(accountRepositoryPort.findById(userId)).willReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserById(userId)
        );

        assertEquals("User not found", exception.getMessage());
        then(accountRepositoryPort).should().findById(userId);
    }
}
