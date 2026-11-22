package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendPasswordChangedUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
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

        emailSender.send(new EmailSendPayload(TEMPLATE, email, subject, variables));
    }
}
