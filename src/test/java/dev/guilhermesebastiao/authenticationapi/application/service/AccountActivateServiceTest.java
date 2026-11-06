package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.AccountActivationInput;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.IncorrectOtpException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountActivateServiceTest {

    @Mock
    private AccountActivationRepositoryPort accountActivationRepository;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private SecureHasherPort secureHasher;

    private AccountActivateService service;

    @BeforeEach
    void setUp() {
        service = new AccountActivateService(
                accountActivationRepository,
                accountRepository,
                passwordEncoder,
                secureHasher
        );
    }

    @Test
    void shouldActivateAccountWhenActivationDataIsValid() {
        String activationToken = "activation-token";
        String activationTokenHash = "activation-token-hash";
        String otpCode = "123456";
        String otpCodeHash = "otp-code-hash";

        Account account = new Account();
        account.setStatus(AccountStatus.PENDING);

        AccountActivation activation = new AccountActivation();
        activation.setAccount(account);
        activation.setOtpCodeHash(otpCodeHash);
        activation.setExpiresAt(Instant.now().plusSeconds(300));

        AccountActivationInput command = new AccountActivationInput(activationToken, otpCode);

        when(secureHasher.hash(activationToken))
                .thenReturn(activationTokenHash);

        when(accountActivationRepository.findByActivationTokenHash(activationTokenHash))
                .thenReturn(Optional.of(activation));

        when(passwordEncoder.matches(otpCode, otpCodeHash))
                .thenReturn(true);

        service.execute(command);

        assertThat(account.getStatus())
                .isEqualTo(AccountStatus.ACTIVATED);

        assertThat(activation.getActivatedAt())
                .isNotNull();

        verify(accountRepository).save(account);
        verify(accountActivationRepository).save(activation);
    }

    @Test
    void shouldThrowWhenActivationTokenDoesNotExist() {
        String activationToken = "invalid-token";
        String activationTokenHash = "invalid-token-hash";

        AccountActivationInput command = new AccountActivationInput(activationToken, "123456");

        when(secureHasher.hash(activationToken))
                .thenReturn(activationTokenHash);

        when(accountActivationRepository.findByActivationTokenHash(activationTokenHash))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(AccountActivationNotFoundException.class);

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(accountRepository);

        verify(accountActivationRepository, never())
                .save(Collections.singletonList(any()));
    }

    @Test
    void shouldThrowWhenAccountIsAlreadyActivated() {
        AccountActivation activation = new AccountActivation();

        activation.setActivatedAt(Instant.now().minusSeconds(60));

        activation.setExpiresAt(Instant.now().plusSeconds(300));

        AccountActivationInput command = new AccountActivationInput("activation-token", "123456");

        when(secureHasher.hash(command.activationToken()))
                .thenReturn("activation-token-hash");

        when(accountActivationRepository.findByActivationTokenHash("activation-token-hash"))
                .thenReturn(Optional.of(activation));

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(AccountAlreadyActivatedException.class);

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(accountRepository);

        verify(accountActivationRepository, never())
                .save(Collections.singletonList(any()));
    }

    @Test
    void shouldThrowWhenAccountActivationIsExpired() {
        AccountActivation activation = new AccountActivation();

        activation.setExpiresAt(Instant.now().minusSeconds(60));

        AccountActivationInput command = new AccountActivationInput("activation-token", "123456");

        when(secureHasher.hash(command.activationToken()))
                .thenReturn("activation-token-hash");

        when(accountActivationRepository.findByActivationTokenHash("activation-token-hash"))
                .thenReturn(Optional.of(activation));

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(AccountActivationExpiredException.class);

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(accountRepository);

        verify(accountActivationRepository, never())
                .save(Collections.singletonList(any()));
    }

    @Test
    void shouldThrowWhenOtpIsIncorrect() {
        String otpCode = "123456";
        String otpCodeHash = "otp-code-hash";

        Account account = new Account();
        account.setStatus(AccountStatus.PENDING);

        AccountActivation activation = new AccountActivation();

        activation.setAccount(account);
        activation.setOtpCodeHash(otpCodeHash);
        activation.setExpiresAt(Instant.now().plusSeconds(300));

        AccountActivationInput command = new AccountActivationInput("activation-token", otpCode);

        when(secureHasher.hash(command.activationToken()))
                .thenReturn("activation-token-hash");

        when(accountActivationRepository.findByActivationTokenHash("activation-token-hash"))
                .thenReturn(Optional.of(activation));

        when(passwordEncoder.matches(otpCode, otpCodeHash))
                .thenReturn(false);

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(IncorrectOtpException.class);

        assertThat(account.getStatus())
                .isEqualTo(AccountStatus.PENDING);

        assertThat(activation.getActivatedAt())
                .isNull();

        verify(accountRepository, never())
                .save(any());

        verify(accountActivationRepository, never())
                .save(Collections.singletonList(any()));
    }
}