package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPayload;

public interface EmailSenderPort {
    void send(EmailSendPayload payload);
}
