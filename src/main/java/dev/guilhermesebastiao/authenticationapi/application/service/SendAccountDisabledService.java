package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.payload.EmailSendPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SendAccountDisabledUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SendAccountDisabledService implements SendAccountDisabledUseCase {
    private static final String TEMPLATE = "account-disabled";

    private final EmailSenderPort emailSender;

    public SendAccountDisabledService(
            EmailSenderPort emailSender
    ) {
        this.emailSender = emailSender;
    }

    @Override
    public void execute(String email) {
        String subject = "Sua conta foi desativada";

        Map<String, Object> variables = Map.of("email", email);

        emailSender.send(EmailSendPayload.of(TEMPLATE, email, subject, variables));
    }
}
