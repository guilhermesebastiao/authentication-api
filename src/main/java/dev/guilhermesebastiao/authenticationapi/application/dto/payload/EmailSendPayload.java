package dev.guilhermesebastiao.authenticationapi.application.dto.payload;

import java.util.Map;

public record EmailSendPayload(
        String template,
        String recipient,
        String subject,
        Map<String, Object> variables
) {
    public static EmailSendPayload of(
            String template,
            String recipient,
            String subject,
            Map<String, Object> variables
    ) {
        return new EmailSendPayload(template, recipient, subject, variables);
    }
}
