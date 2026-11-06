package dev.guilhermesebastiao.authenticationapi.adapter.in.consumer;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.JavaMailSenderAdapter;
import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.EmailSendPaylod;
import dev.guilhermesebastiao.authenticationapi.infrastructure.config.EmailQueueConfig;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailSendConsumer {
    private final JavaMailSenderAdapter sendMail;

    @RabbitListener(queues = EmailQueueConfig.EMAIL_QUEUE)
    public void consume(EmailSendPaylod command) throws MessagingException {
        sendMail.send(command);
    }
}
