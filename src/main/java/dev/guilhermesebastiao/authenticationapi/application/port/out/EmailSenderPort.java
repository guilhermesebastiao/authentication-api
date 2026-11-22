package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.application.dto.payload.EmailSendPayload;

public interface EmailSenderPort {
    void send(EmailSendPayload payload);
}
