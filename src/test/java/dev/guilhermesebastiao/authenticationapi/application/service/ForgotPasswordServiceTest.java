package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.ForgotPasswordRepositoryPort;

import dev.guilhermesebastiao.authenticationapi.application.port.in.SendForgotUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.ForgotPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ForgotPasswordServiceTest {

    @Mock
    private SendForgotUseCase sendForgotUseCase;

    @Mock
    private ForgotPasswordRepositoryPort recoverPasswordRepository;

    @Mock
    private SecureRandomGeneratorPort secureRandomGenerator;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SecureHasherPort secureHasher;

    @Mock
    private SecureRandom secureRandom;

    @InjectMocks
    private ForgotPasswordService service;

    @Test
    @DisplayName("Should create and send a password recovery request for an existing account")
    void givenExistingAccount_whenCreateForgotPassword_thenRecoveryIsGeneratedSavedAndEmailed() {
        String email = "user@example.com";
        Account account = new Account();
        account.setEmail(email);
        Instant beforeExecution = Instant.now();

        given(secureRandom.nextInt(1_000_000)).willReturn(1234);
        given(secureRandomGenerator.generate(32)).willReturn("recover-token");
        given(accountRepository.findByEmail(email))
                .willReturn(Optional.of(account));

        given(secureHasher.hash("recover-token"))
                .willReturn("recover-token-hash");

        given(passwordEncoder.hash("001234"))
                .willReturn("otp-hash");

        given(recoverPasswordRepository.save(any(ForgotPassword.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        ForgotPasswordOutput result = service.execute(email);
        Instant afterExecution = Instant.now();

        ArgumentCaptor<ForgotPassword> recoveryCaptor = ArgumentCaptor.forClass(ForgotPassword.class);
        then(recoverPasswordRepository).should().save(recoveryCaptor.capture());
        ForgotPassword savedRecovery = recoveryCaptor.getValue();

        assertEquals(account.getEmail(), result.email());
        assertEquals("recover-token", result.forgotToken());
        assertEquals(account, savedRecovery.getAccount());
        assertEquals("recover-token-hash", savedRecovery.getForgotTokenHash());
        assertEquals("otp-hash", savedRecovery.getOtpHash());
        assertNotNull(result.resendAvailableAt());
        assertNotNull(result.expiresAt());
        assertTrue(result.resendAvailableAt().isAfter(beforeExecution.plus(30, ChronoUnit.SECONDS)));
        assertTrue(result.resendAvailableAt().isBefore(afterExecution.plus(90, ChronoUnit.SECONDS)));
        assertTrue(result.expiresAt().isAfter(beforeExecution.plus(14, ChronoUnit.MINUTES)));
        assertTrue(result.expiresAt().isBefore(afterExecution.plus(16, ChronoUnit.MINUTES)));
        assertTrue(result.expiresAt().isAfter(result.resendAvailableAt()));

        then(accountRepository).should().findByEmail(email);
        then(secureHasher).should().hash("recover-token");
        then(passwordEncoder).should().hash("001234");
        then(sendForgotUseCase).should()
                .execute(email, "001234", result.expiresAt());
    }

    @Test
    @DisplayName("Should reject recovery creation when the account does not exist")
    void givenUnknownAccountEmail_whenCreateForgotPassword_thenAccountNotFoundExceptionIsThrown() {
        String email = "unknown@example.com";

        given(accountRepository.findByEmail(email))
                .willReturn(java.util.Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> service.execute(email));

        then(accountRepository).should().findByEmail(email);
        then(secureHasher).shouldHaveNoInteractions();
        then(passwordEncoder).shouldHaveNoInteractions();
        then(recoverPasswordRepository).should(never()).save(any(ForgotPassword.class));
        then(sendForgotUseCase).shouldHaveNoInteractions();
    }
}
