package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CreateAccountActivationServiceTest {

    @Mock
    private SendAccountActivationUseCase sendAccountActivationUseCase;

    @Mock
    private AccountActivationRepositoryPort accountActivationRepository;

    @Mock
    private SecureRandomGeneratorPort secureRandomGenerator;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SecureHasherPort secureHasher;

    @Mock
    private SecureRandom secureRandom;

    @InjectMocks
    private CreateAccountActivationService service;

    @Test
    @DisplayName("Should create and send an account activation for a pending account")
    void givenPendingAccount_whenCreateActivation_thenActivationIsSavedEmailedAndReturned() {
        Account account = new Account();
        account.setEmail("user@example.com");
        account.setStatus(AccountStatus.PENDING);

        given(secureRandom.nextInt(1_000_000))
                .willReturn(1234);

        given(passwordEncoder.hash("001234"))
                .willReturn("otp-code-hash");

        given(secureRandomGenerator.generate(32))
                .willReturn("activation-token");

        given(secureHasher.hash("activation-token"))
                .willReturn("activation-token-hash");

        Instant beforeExecution = Instant.now();

        AccountActivationOutput result = service.execute(account);
        Instant afterExecution = Instant.now();

        ArgumentCaptor<AccountActivation> activationCaptor = ArgumentCaptor.forClass(AccountActivation.class);

        then(accountActivationRepository).should().save(activationCaptor.capture());
        AccountActivation savedActivation = activationCaptor.getValue();

        assertEquals(account, savedActivation.getAccount());
        assertEquals("activation-token-hash", savedActivation.getActivationTokenHash());
        assertEquals("otp-code-hash", savedActivation.getOtpCodeHash());

        assertTrue(savedActivation.getExpiresAt()
                .isAfter(beforeExecution.plus(14, ChronoUnit.MINUTES)));

        assertTrue(savedActivation.getExpiresAt()
                .isBefore(afterExecution.plus(16, ChronoUnit.MINUTES)));

        assertTrue(savedActivation.getResendAvailableAt()
                .isAfter(beforeExecution.plus(30, ChronoUnit.SECONDS)));

        assertTrue(savedActivation.getResendAvailableAt()
                .isBefore(afterExecution.plus(90, ChronoUnit.SECONDS)));

        assertEquals("activation-token", result.activationToken());
        assertEquals(account.getEmail(), result.email());
        assertEquals(savedActivation.getExpiresAt(), result.expiresAt());
        assertEquals(savedActivation.getResendAvailableAt(), result.resendAvailableAt());

        then(sendAccountActivationUseCase).should()
                .execute(account.getEmail(), "001234", savedActivation.getExpiresAt());

        then(passwordEncoder).should().hash("001234");
        then(secureRandomGenerator).should().generate(32);
        then(secureHasher).should().hash("activation-token");
    }
}
