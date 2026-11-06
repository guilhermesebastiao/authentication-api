package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.ValidateForgotPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.port.out.ForgotPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoveryPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ForgotPasswordExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.ForgotPasswordNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.IncorrectOtpException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ValidateRecoveryPasswordServiceTest {
    @Mock private RecoveryPasswordRepositoryPort recoveryPasswordRepository;
    @Mock private ForgotPasswordRepositoryPort forgotPasswordRepository;
    @Mock private SecureRandomGeneratorPort secureRandomGenerator;
    @Mock private PasswordEncoderPort passwordEncoder;
    @Mock private SecureHasherPort secureHasher;
    @InjectMocks private ValidateForgotPasswordService service;

    @Test
    void verifiesForgotRequestAndCreatesRecoveryToken() {
        var input = new ValidateForgotPasswordInput("forgot-token", "123456");
        var forgot = new ForgotPassword();
        forgot.setOtpHash("otp-hash");
        forgot.setExpiresAt(Instant.now().plusSeconds(300));
        given(secureHasher.hash(input.forgotToken())).willReturn("forgot-hash", "recovery-hash");
        given(forgotPasswordRepository.findByForgotTokenHash("forgot-hash")).willReturn(Optional.of(forgot));
        given(passwordEncoder.matches(input.otp(), "otp-hash")).willReturn(true);
        given(forgotPasswordRepository.save(forgot)).willReturn(forgot);
        given(secureRandomGenerator.generate(32)).willReturn("recovery-token");
        given(recoveryPasswordRepository.save(any(RecoveryPassword.class))).willAnswer(invocation -> invocation.getArgument(0));

        var output = service.execute(input);

        assertEquals("recovery-token", output.recoveryToken());
        assertFalse(output.usedAt());
        then(forgotPasswordRepository).should().save(forgot);
        then(recoveryPasswordRepository).should().save(any(RecoveryPassword.class));
    }

    @Test
    void rejectsUnknownForgotToken() {
        var input = new ValidateForgotPasswordInput("unknown", "123456");
        given(secureHasher.hash(input.forgotToken())).willReturn("hash");
        given(forgotPasswordRepository.findByForgotTokenHash("hash")).willReturn(Optional.empty());
        assertThrows(ForgotPasswordNotFoundException.class, () -> service.execute(input));
        then(passwordEncoder).shouldHaveNoInteractions();
    }

    @Test
    void rejectsExpiredForgotRequest() {
        var input = new ValidateForgotPasswordInput("expired", "123456");
        var forgot = new ForgotPassword();
        forgot.setExpiresAt(Instant.now().minusSeconds(1));
        given(secureHasher.hash(input.forgotToken())).willReturn("hash");
        given(forgotPasswordRepository.findByForgotTokenHash("hash")).willReturn(Optional.of(forgot));
        assertThrows(ForgotPasswordExpiredException.class, () -> service.execute(input));
        then(passwordEncoder).shouldHaveNoInteractions();
    }

    @Test
    void rejectsIncorrectOtp() {
        var input = new ValidateForgotPasswordInput("forgot", "000000");
        var forgot = new ForgotPassword();
        forgot.setOtpHash("otp-hash");
        forgot.setExpiresAt(Instant.now().plusSeconds(60));
        given(secureHasher.hash(input.forgotToken())).willReturn("hash");
        given(forgotPasswordRepository.findByForgotTokenHash("hash")).willReturn(Optional.of(forgot));
        given(passwordEncoder.matches(input.otp(), "otp-hash")).willReturn(false);
        assertThrows(IncorrectOtpException.class, () -> service.execute(input));
        then(forgotPasswordRepository).shouldHaveNoMoreInteractions();
    }
}
