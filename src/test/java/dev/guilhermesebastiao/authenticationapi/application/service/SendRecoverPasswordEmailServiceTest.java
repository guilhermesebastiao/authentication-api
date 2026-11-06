package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPaylod;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SendRecoverPasswordEmailServiceTest {

    @Mock
    private EmailSenderPort emailSender;

    @InjectMocks
    private SendForgotService service;

    @Test
    @DisplayName("Should send the password recovery email with the recipient and OTP")
    void givenEmailAndOtp_whenSendRecoverPasswordEmail_thenEmailPayloadIsSent() {
        String email = "user@example.com";
        String otpCode = "654321";
        Instant expiresAt = Instant.parse("2026-10-04T12:15:00Z");

        service.execute(email, otpCode, expiresAt);

        ArgumentCaptor<EmailSendPaylod> payloadCaptor = ArgumentCaptor.forClass(EmailSendPaylod.class);
        then(emailSender).should().send(payloadCaptor.capture());

        EmailSendPaylod payload = payloadCaptor.getValue();
        assertEquals("recover-password-template", payload.template());
        assertEquals(email, payload.recipient());
        assertEquals("Solicitação de redefinição de senha", payload.subject());
        assertEquals(Map.of("email", email, "otp", otpCode), payload.variables());
    }
}
