package dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload;

import java.util.Map;

public record EmailSendPaylod(
        String template,
        String recipient,
        String subject,
        Map<String, Object> variables
) {
}
