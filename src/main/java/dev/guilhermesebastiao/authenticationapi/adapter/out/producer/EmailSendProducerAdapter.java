package dev.guilhermesebastiao.authenticationapi.adapter.out.producer;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.EmailSendPaylod;
import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;
import dev.guilhermesebastiao.authenticationapi.infrastructure.config.EmailQueueConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailSendProducerAdapter implements EmailSenderPort {
    private final RabbitTemplate rabbitTemplate;


    @Override
    public void send(EmailSendPaylod payload) {
        rabbitTemplate.convertAndSend(EmailQueueConfig.EMAIL_EXCHANGE, EmailQueueConfig.EMAIL_ROUTING_KEY, payload);
    }
}
