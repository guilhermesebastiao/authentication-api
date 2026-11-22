package dev.guilhermesebastiao.authenticationapi.adapter.out.publisher;

import dev.guilhermesebastiao.authenticationapi.application.dto.payload.EmailSendPayload;
import dev.guilhermesebastiao.authenticationapi.infrastructure.messaging.EmailMessagingConfig;
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
class EmailSendPublisherAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EmailSendPublisherAdapter adapter;

    @Test
    @DisplayName("Should publish the email payload using the configured exchange and routing key")
    void givenEmailPayload_whenSend_thenPublishToEmailRoute() {
        EmailSendPayload payload = new EmailSendPayload(
                "account-activation-template",
                "user@example.com",
                "Activate account",
                Map.of("otpCode", "123456")
        );

        adapter.send(payload);

        then(rabbitTemplate).should().convertAndSend(
                EmailMessagingConfig.EMAIL_EXCHANGE,
                EmailMessagingConfig.EMAIL_ROUTING_KEY,
                payload
        );
    }
}
