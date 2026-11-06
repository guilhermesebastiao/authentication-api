package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPaylod;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendForgotUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;

import java.time.Instant;
import java.util.Map;

public class SendForgotService implements SendForgotUseCase {
    private static final String TEMPLATE = "recover-password-template";

    private final EmailSenderPort emailSender;

    public SendForgotService(EmailSenderPort emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void execute(String email, String otp, Instant expiresAt) {
        String subject = "Solicitação de redefinição de senha";

        Map<String, Object> variables = Map.of(
                "email", email,
                "otp", otp
        );

        emailSender.send(new EmailSendPaylod(TEMPLATE, email, subject, variables));
    }
}
