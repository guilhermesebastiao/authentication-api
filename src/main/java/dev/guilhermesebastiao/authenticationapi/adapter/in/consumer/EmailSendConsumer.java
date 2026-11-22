package dev.guilhermesebastiao.authenticationapi.adapter.in.consumer;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.adapter.JavaMailSenderAdapter;
import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPayload;
import dev.guilhermesebastiao.authenticationapi.infrastructure.messaging.EmailMessagingConfig;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailSendConsumer {
    private final JavaMailSenderAdapter sendMail;

    @RabbitListener(queues = EmailMessagingConfig.EMAIL_QUEUE)
    public void consume(EmailSendPayload command) throws MessagingException {
        sendMail.send(command);
    }
}
