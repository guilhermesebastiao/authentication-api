package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPaylod;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountWelcomeUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;

import java.util.Map;

public class SendAccountWelcomeService implements SendAccountWelcomeUseCase {
    private static final String TEMPLATE = "account-welcome";

    private final EmailSenderPort emailSender;

    public SendAccountWelcomeService(
            EmailSenderPort emailSender
    ) {
        this.emailSender = emailSender;
    }

    @Override
    public void execute(String email) {
        String subject = "Bem-vindo!";

        Map<String, Object> variables = Map.of("email", email);

        emailSender.send(new EmailSendPaylod(TEMPLATE, email, subject, variables));
    }
}
