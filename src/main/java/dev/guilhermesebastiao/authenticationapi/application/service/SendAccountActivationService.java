package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class SendAccountActivationService implements SendAccountActivationUseCase {
    private static final String TEMPLATE = "account-activation";

    private final EmailSenderPort emailSender;

    public SendAccountActivationService(
            EmailSenderPort emailSender
    ) {
        this.emailSender = emailSender;
    }

    @Override
    public void execute(String email, String otp, Instant expiresAt) {
        String subject = "Confirme seu e-mail para ativar sua conta";

        Map<String, Object> variables = Map.of(
                "email", email,
                "otp", otp
        );

        emailSender.send(new EmailSendPayload(TEMPLATE, email, subject, variables));
    }
}
