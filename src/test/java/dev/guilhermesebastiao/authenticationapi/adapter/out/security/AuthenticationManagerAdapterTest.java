package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAuthenticationExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountDisabledException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountNotActivatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class AuthenticationManagerAdapterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationManagerAdapter adapter;

    @Test
    @DisplayName("Should authenticate using the supplied email and password")
    void givenCredentials_whenAuthenticate_thenPassUsernamePasswordToken() {
        String email = "user@example.com";
        String password = "correct-password";

        adapter.authenticate(email, password);

        ArgumentCaptor<Authentication> authenticationCaptor =
                ArgumentCaptor.forClass(Authentication.class);
        then(authenticationManager).should().authenticate(authenticationCaptor.capture());

        Authentication authentication = authenticationCaptor.getValue();
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals(email, authentication.getName());
        assertEquals(password, authentication.getCredentials());
    }

    @Test
    @DisplayName("Should translate bad credentials into an invalid credentials exception")
    void givenBadCredentials_whenAuthenticate_thenThrowInvalidCredentialsException() {
        willThrow(new BadCredentialsException("Invalid credentials"))
                .given(authenticationManager)
                .authenticate(any(Authentication.class));

        assertThrows(
                AccountInvalidCredentialsException.class,
                () -> adapter.authenticate("user@example.com", "wrong-password")
        );
    }

    @Test
    @DisplayName("Should translate an unknown user into an invalid credentials exception")
    void givenUnknownUser_whenAuthenticate_thenThrowInvalidCredentialsException() {
        willThrow(new UsernameNotFoundException("User not found"))
                .given(authenticationManager)
                .authenticate(any(Authentication.class));

        assertThrows(
                AccountInvalidCredentialsException.class,
                () -> adapter.authenticate("unknown@example.com", "password")
        );
    }

    @Test
    @DisplayName("Should translate a disabled account into an account disabled exception")
    void givenDisabledAccount_whenAuthenticate_thenThrowAccountDisabledException() {
        willThrow(new DisabledException("Account disabled"))
                .given(authenticationManager)
                .authenticate(any(Authentication.class));

        assertThrows(
                AccountDisabledException.class,
                () -> adapter.authenticate("user@example.com", "password")
        );
    }

    @Test
    @DisplayName("Should translate a locked account into an account not activated exception")
    void givenLockedAccount_whenAuthenticate_thenThrowAccountNotActivatedException() {
        willThrow(new LockedException("Account locked"))
                .given(authenticationManager)
                .authenticate(any(Authentication.class));

        assertThrows(
                AccountNotActivatedException.class,
                () -> adapter.authenticate("user@example.com", "password")
        );
    }

    @Test
    @DisplayName("Should translate an expired account into an authentication expired exception")
    void givenExpiredAccount_whenAuthenticate_thenThrowAuthenticationExpiredException() {
        willThrow(new AccountExpiredException("Account expired"))
                .given(authenticationManager)
                .authenticate(any(Authentication.class));

        assertThrows(
                AccountAuthenticationExpiredException.class,
                () -> adapter.authenticate("user@example.com", "password")
        );
    }

    @Test
    @DisplayName("Should translate expired credentials into an authentication expired exception")
    void givenExpiredCredentials_whenAuthenticate_thenThrowAuthenticationExpiredException() {
        willThrow(new CredentialsExpiredException("Credentials expired"))
                .given(authenticationManager)
                .authenticate(any(Authentication.class));

        assertThrows(
                AccountAuthenticationExpiredException.class,
                () -> adapter.authenticate("user@example.com", "password")
        );
    }
}
