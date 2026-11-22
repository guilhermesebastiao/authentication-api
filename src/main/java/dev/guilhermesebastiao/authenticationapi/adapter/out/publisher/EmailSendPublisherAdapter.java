package dev.guilhermesebastiao.authenticationapi.adapter.out.publisher;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;
import dev.guilhermesebastiao.authenticationapi.infrastructure.messaging.EmailMessagingConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailSendPublisherAdapter implements EmailSenderPort {
    private final RabbitTemplate rabbitTemplate;


    @Override
    public void send(EmailSendPayload payload) {
        rabbitTemplate.convertAndSend(EmailMessagingConfig.EMAIL_EXCHANGE, EmailMessagingConfig.EMAIL_ROUTING_KEY, payload);
    }
}
