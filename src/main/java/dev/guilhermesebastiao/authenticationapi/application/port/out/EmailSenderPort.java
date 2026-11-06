package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.EmailSendPaylod;

public interface EmailSenderPort {
    void send(EmailSendPaylod payload);
}
