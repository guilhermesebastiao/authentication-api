package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationResendNotAvailableException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.time.Instant;
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
class ResendAccountActivationServiceTest {

    @Mock
    private SendAccountActivationUseCase sendAccountActivationUseCase;

    @Mock
    private AccountActivationRepositoryPort accountActivationRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SecureHasherPort secureHasher;

    @Mock
    private SecureRandom secureRandom;

    @InjectMocks
    private ResendAccountActivationService service;

    @Test
    @DisplayName("Should generate and send a new OTP for an active account activation")
    void givenActiveAccountActivation_whenResendActivationEmail_thenOtpIsRegeneratedAndEmailIsSent() {
        String activationToken = "activation-token";
        String activationTokenHash = "activation-token-hash";
        Account account = new Account();
        account.setEmail("user@example.com");
        AccountActivation accountActivation = new AccountActivation();
        accountActivation.setAccount(account);
        accountActivation.setOtpHash("old-otp-hash");
        accountActivation.setExpiresAt(Instant.now().plusSeconds(600));
        accountActivation.setResendAvailableAt(Instant.now().minusSeconds(60));
        Instant beforeExecution = Instant.now();

        given(secureHasher.hash(activationToken))
                .willReturn(activationTokenHash);
        given(accountActivationRepository.findByActivationTokenHash(activationTokenHash))
                .willReturn(Optional.of(accountActivation));
        given(secureRandom.nextInt(1_000_000))
                .willReturn(1234);
        given(passwordEncoder.hash("001234"))
                .willReturn("new-otp-code-hash");
        given(accountActivationRepository.save(accountActivation))
                .willReturn(accountActivation);

        AccountActivationOutput result = service.execute(activationToken);
        Instant afterExecution = Instant.now();

        assertEquals(activationToken, result.activationToken());
        assertEquals(account.getEmail(), result.email());
        assertEquals("new-otp-code-hash", accountActivation.getOtpHash());
        assertNotNull(accountActivation.getResendAvailableAt());
        assertTrue(accountActivation.getResendAvailableAt().isAfter(beforeExecution.plusSeconds(30)));
        assertTrue(accountActivation.getResendAvailableAt().isBefore(afterExecution.plusSeconds(90)));
        assertEquals(accountActivation.getExpiresAt(), result.expiresAt());
        assertEquals(accountActivation.getResendAvailableAt(), result.resendAvailableAt());

        then(secureHasher).should().hash(activationToken);
        then(accountActivationRepository).should()
                .findByActivationTokenHash(activationTokenHash);
        then(secureRandom).should().nextInt(1_000_000);
        then(passwordEncoder).should().hash("001234");
        then(accountActivationRepository).should().save(accountActivation);
        then(sendAccountActivationUseCase).should()
                .execute(account.getEmail(), "001234", accountActivation.getExpiresAt());
    }

    @Test
    @DisplayName("Should reject an unknown activation token")
    void givenUnknownActivationToken_whenResendActivationEmail_thenThrowNotFoundException() {
        String activationToken = "unknown-activation-token";
        String activationTokenHash = "unknown-activation-token-hash";

        given(secureHasher.hash(activationToken))
                .willReturn(activationTokenHash);
        given(accountActivationRepository.findByActivationTokenHash(activationTokenHash))
                .willReturn(Optional.empty());

        assertThrows(AccountActivationNotFoundException.class, () -> service.execute(activationToken));

        then(accountActivationRepository).should()
                .findByActivationTokenHash(activationTokenHash);
        then(accountActivationRepository).should(never()).save(any(AccountActivation.class));
        then(passwordEncoder).shouldHaveNoInteractions();
        then(secureRandom).shouldHaveNoInteractions();
        then(sendAccountActivationUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject an expired account activation")
    void givenExpiredAccountActivation_whenResendActivationEmail_thenThrowExpiredException() {
        String activationToken = "expired-activation-token";
        String activationTokenHash = "expired-activation-token-hash";
        AccountActivation accountActivation = accountActivation(
                Instant.now().minusSeconds(60),
                Instant.now().minusSeconds(60),
                null
        );

        given(secureHasher.hash(activationToken))
                .willReturn(activationTokenHash);
        given(accountActivationRepository.findByActivationTokenHash(activationTokenHash))
                .willReturn(Optional.of(accountActivation));

        assertThrows(AccountActivationExpiredException.class, () -> service.execute(activationToken));

        then(accountActivationRepository).should()
                .findByActivationTokenHash(activationTokenHash);
        then(accountActivationRepository).should(never()).save(any(AccountActivation.class));
        then(passwordEncoder).shouldHaveNoInteractions();
        then(secureRandom).shouldHaveNoInteractions();
        then(sendAccountActivationUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject an already activated account")
    void givenAlreadyActivatedAccount_whenResendActivationEmail_thenThrowAlreadyActivatedException() {
        String activationToken = "activated-token";
        String activationTokenHash = "activated-token-hash";
        AccountActivation accountActivation = accountActivation(
                Instant.now().plusSeconds(600),
                Instant.now().minusSeconds(60),
                Instant.now().minusSeconds(120)
        );

        given(secureHasher.hash(activationToken))
                .willReturn(activationTokenHash);
        given(accountActivationRepository.findByActivationTokenHash(activationTokenHash))
                .willReturn(Optional.of(accountActivation));

        assertThrows(AccountAlreadyActivatedException.class, () -> service.execute(activationToken));

        then(accountActivationRepository).should()
                .findByActivationTokenHash(activationTokenHash);
        then(accountActivationRepository).should(never()).save(any(AccountActivation.class));
        then(passwordEncoder).shouldHaveNoInteractions();
        then(secureRandom).shouldHaveNoInteractions();
        then(sendAccountActivationUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Should reject a resend request before the allowed window")
    void givenActivationBeforeResendWindow_whenResendActivationEmail_thenThrowResendNotAvailableException() {
        String activationToken = "not-available-activation-token";
        String activationTokenHash = "not-available-activation-token-hash";
        AccountActivation accountActivation = accountActivation(
                Instant.now().plusSeconds(600),
                Instant.now().plusSeconds(60),
                null
        );

        given(secureHasher.hash(activationToken))
                .willReturn(activationTokenHash);
        given(accountActivationRepository.findByActivationTokenHash(activationTokenHash))
                .willReturn(Optional.of(accountActivation));

        assertThrows(
                AccountActivationResendNotAvailableException.class,
                () -> service.execute(activationToken)
        );

        then(accountActivationRepository).should()
                .findByActivationTokenHash(activationTokenHash);
        then(accountActivationRepository).should(never()).save(any(AccountActivation.class));
        then(passwordEncoder).shouldHaveNoInteractions();
        then(secureRandom).shouldHaveNoInteractions();
        then(sendAccountActivationUseCase).shouldHaveNoInteractions();
    }

    private AccountActivation accountActivation(Instant expiresAt, Instant resendAvailableAt, Instant activatedAt) {
        AccountActivation accountActivation = new AccountActivation();
        accountActivation.setExpiresAt(expiresAt);
        accountActivation.setResendAvailableAt(resendAvailableAt);
        accountActivation.setActivatedAt(activatedAt);
        return accountActivation;
    }
}
