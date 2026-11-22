package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.payload.EmailSendPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountWelcomeUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
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

        emailSender.send(EmailSendPayload.of(TEMPLATE, email, subject, variables));
    }
}
