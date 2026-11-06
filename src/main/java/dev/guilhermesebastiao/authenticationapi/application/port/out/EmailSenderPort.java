package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPaylod;

public interface EmailSenderPort {
    void send(EmailSendPaylod payload);
}
