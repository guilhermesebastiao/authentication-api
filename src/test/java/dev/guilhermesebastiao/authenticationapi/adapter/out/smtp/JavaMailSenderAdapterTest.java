package dev.guilhermesebastiao.authenticationapi.adapter.out.smtp;

import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.AppProperties;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class JavaMailSenderAdapterTest {

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private AppProperties properties;

    @InjectMocks
    private JavaMailSenderAdapter adapter;

    @Test
    @DisplayName("Should render the selected template and send the generated HTML")
    void givenEmailPayload_whenSend_thenRenderedHtmlIsSent() throws Exception {
        EmailSendPaylod payload = new EmailSendPaylod(
                "account-activation-template",
                "user@example.com",
                "Activate account",
                Map.of("otpCode", "123456")
        );

        String renderedHtml = "<p>Your activation code is 123456</p>";
        MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));

        given(properties.emailFrom()).willReturn("no-reply@example.com");
        given(mailSender.createMimeMessage()).willReturn(message);

        given(templateEngine.process(eq(payload.template()), any(Context.class)))
                .willReturn(renderedHtml);

        adapter.send(payload);

        then(templateEngine).should().process(eq(payload.template()), any(Context.class));
        then(mailSender).should().send(message);

        MimeMultipart rootContent = (MimeMultipart) message.getContent();
        BodyPart relatedPart = rootContent.getBodyPart(0);
        MimeMultipart relatedContent = (MimeMultipart) relatedPart.getContent();
        BodyPart bodyPart = relatedContent.getBodyPart(0);

        assertEquals(renderedHtml, bodyPart.getContent());
    }

    @Test
    @DisplayName("Should populate the template context with the email variables")
    void givenEmailVariables_whenSend_thenTemplateContextContainsThem() throws Exception {
        Map<String, Object> variables = Map.of(
                "email", "user@example.com",
                "otpCode", "123456"
        );

        EmailSendPaylod payload = new EmailSendPaylod(
                "account-activation-template",
                "user@example.com",
                "Activate account",
                variables
        );

        given(properties.emailFrom()).willReturn("no-reply@example.com");

        given(mailSender.createMimeMessage())
                .willReturn(new MimeMessage(Session.getInstance(new Properties())));

        given(templateEngine.process(eq(payload.template()), any(Context.class)))
                .willReturn("<p>activation</p>");

        adapter.send(payload);

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        then(templateEngine).should().process(eq(payload.template()), contextCaptor.capture());

        Context context = contextCaptor.getValue();
        assertEquals("user@example.com", context.getVariable("email"));
        assertEquals("123456", context.getVariable("otpCode"));
    }

    @Test
    @DisplayName("Should configure the sender, recipient and subject")
    void givenEmailPayload_whenSend_thenMessageMetadataIsConfigured() throws Exception {
        EmailSendPaylod payload = new EmailSendPaylod(
                "recover-password-template",
                "user@example.com",
                "Recover password",
                Map.of()
        );

        MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));

        given(properties.emailFrom()).willReturn("no-reply@example.com");
        given(mailSender.createMimeMessage()).willReturn(message);

        given(templateEngine.process(eq(payload.template()), any(Context.class)))
                .willReturn("<p>recover</p>");

        adapter.send(payload);

        assertEquals("no-reply@example.com", message.getFrom()[0].toString());
        assertEquals("user@example.com", message.getRecipients(Message.RecipientType.TO)[0].toString());
        assertEquals("Recover password", message.getSubject());
        then(mailSender).should().send(message);
    }
}
