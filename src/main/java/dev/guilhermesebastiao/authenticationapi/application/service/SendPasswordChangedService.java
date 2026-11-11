package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPaylod;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendPasswordChangedUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;

import java.util.Map;

public class SendPasswordChangedService implements SendPasswordChangedUseCase {
    private static final String TEMPLATE = "password-changed";

    private final EmailSenderPort emailSender;

    public SendPasswordChangedService(
            EmailSenderPort emailSender
    ) {
        this.emailSender = emailSender;
    }

    @Override
    public void execute(String email) {
        String subject = "Sua senha foi alterada";

        Map<String, Object> variables = Map.of("email", email);

        emailSender.send(new EmailSendPaylod(TEMPLATE, email, subject, variables));
    }
}
