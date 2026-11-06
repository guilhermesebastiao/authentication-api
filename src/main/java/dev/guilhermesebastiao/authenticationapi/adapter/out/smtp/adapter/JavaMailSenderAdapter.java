package dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPaylod;
import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.AppProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JavaMailSenderAdapter {
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final AppProperties properties;

    public void send(EmailSendPaylod payload) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String templateCreated = createTemplate(payload.variables(), payload.template());

        helper.setFrom(properties.emailFrom());
        helper.setTo(payload.recipient());
        helper.setSubject(payload.subject());
        helper.setText(templateCreated, true);

        mailSender.send(mimeMessage);
    }

    private String createTemplate(Map<String, Object> variables, String template) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }
}
