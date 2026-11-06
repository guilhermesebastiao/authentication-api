package dev.guilhermesebastiao.authenticationapi.adapter.out.producer;

import dev.guilhermesebastiao.authenticationapi.adapter.out.smtp.payload.EmailSendPaylod;
import dev.guilhermesebastiao.authenticationapi.infrastructure.config.EmailQueueConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EmailSendProducerAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EmailSendProducerAdapter adapter;

    @Test
    @DisplayName("Should publish the email payload using the configured exchange and routing key")
    void givenEmailPayload_whenSend_thenPublishToEmailRoute() {
        EmailSendPaylod payload = new EmailSendPaylod(
                "account-activation-template",
                "user@example.com",
                "Activate account",
                Map.of("otpCode", "123456")
        );

        adapter.send(payload);

        then(rabbitTemplate).should().convertAndSend(
                EmailQueueConfig.EMAIL_EXCHANGE,
                EmailQueueConfig.EMAIL_ROUTING_KEY,
                payload
        );
    }
}
