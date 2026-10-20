package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.command.CreateRecoverPasswordCommand;
import br.com.guisebastiao.authenticationapi.application.port.in.SendRecoverPasswordEmailUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import br.com.guisebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.SecureHasherPort;
import br.com.guisebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import br.com.guisebastiao.authenticationapi.application.result.RecoverPasswordResult;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountNotFoundException;
import br.com.guisebastiao.authenticationapi.domain.model.Account;
import br.com.guisebastiao.authenticationapi.domain.model.RecoverPassword;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CreateRecoverPasswordServiceTest {

    @Mock
    private SendRecoverPasswordEmailUseCase sendRecoverPasswordEmailUseCase;

    @Mock
    private RecoverPasswordRepositoryPort recoverPasswordRepository;

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
    private CreateRecoverPasswordService service;

    @Test
    @DisplayName("Should create and send a password recovery request for an existing account")
    void givenExistingAccount_whenCreateRecoverPassword_thenRecoveryIsGeneratedSavedAndEmailed() {
        CreateRecoverPasswordCommand command = new CreateRecoverPasswordCommand("user@example.com");
        Account account = new Account();
        account.setEmail(command.email());
        Instant beforeExecution = Instant.now();

        given(secureRandom.nextInt(1_000_000))
                .willReturn(1234);

        given(secureRandomGenerator.generate(32))
                .willReturn("recover-token");

        given(accountRepository.findByEmail(command.email()))
                .willReturn(Optional.of(account));

        given(secureHasher.hash("recover-token"))
                .willReturn("recover-token-hash");

        given(passwordEncoder.hash("001234"))
                .willReturn("otp-code-hash");

        given(recoverPasswordRepository.save(any(RecoverPassword.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        RecoverPasswordResult result = service.execute(command);
        Instant afterExecution = Instant.now();

        ArgumentCaptor<RecoverPassword> recoveryCaptor = ArgumentCaptor.forClass(RecoverPassword.class);
        then(recoverPasswordRepository).should().save(recoveryCaptor.capture());
        RecoverPassword savedRecovery = recoveryCaptor.getValue();

        assertEquals(account.getEmail(), result.email());
        assertEquals("recover-token", result.recoverToken());
        assertFalse(result.isVerified());
        assertEquals(account, savedRecovery.getAccount());
        assertEquals("recover-token-hash", savedRecovery.getRecoverTokenHash());
        assertEquals("otp-code-hash", savedRecovery.getOtpCodeHash());
        assertNotNull(result.resendAvailableAt());
        assertNotNull(result.expiresAt());
        assertTrue(result.resendAvailableAt().isAfter(beforeExecution.plus(30, ChronoUnit.SECONDS)));
        assertTrue(result.resendAvailableAt().isBefore(afterExecution.plus(90, ChronoUnit.SECONDS)));
        assertTrue(result.expiresAt().isAfter(beforeExecution.plus(14, ChronoUnit.MINUTES)));
        assertTrue(result.expiresAt().isBefore(afterExecution.plus(16, ChronoUnit.MINUTES)));
        assertTrue(result.expiresAt().isAfter(result.resendAvailableAt()));

        then(secureRandom).should().nextInt(1_000_000);
        then(secureRandomGenerator).should().generate(32);
        then(accountRepository).should().findByEmail(command.email());
        then(secureHasher).should().hash("recover-token");
        then(passwordEncoder).should().hash("001234");
        then(sendRecoverPasswordEmailUseCase).should()
                .execute(command.email(), "001234", result.expiresAt());
    }

    @Test
    @DisplayName("Should reject recovery creation when the account does not exist")
    void givenUnknownAccountEmail_whenCreateRecoverPassword_thenAccountNotFoundExceptionIsThrown() {
        CreateRecoverPasswordCommand command = new CreateRecoverPasswordCommand("unknown@example.com");

        given(secureRandom.nextInt(1_000_000))
                .willReturn(1234);

        given(secureRandomGenerator.generate(32))
                .willReturn("recover-token");

        given(accountRepository.findByEmail(command.email()))
                .willReturn(java.util.Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> service.execute(command));

        then(secureRandom).should().nextInt(1_000_000);
        then(secureRandomGenerator).should().generate(32);
        then(accountRepository).should().findByEmail(command.email());
        then(secureHasher).shouldHaveNoInteractions();
        then(passwordEncoder).shouldHaveNoInteractions();
        then(recoverPasswordRepository).should(never()).save(any(RecoverPassword.class));
        then(sendRecoverPasswordEmailUseCase).shouldHaveNoInteractions();
    }
}
