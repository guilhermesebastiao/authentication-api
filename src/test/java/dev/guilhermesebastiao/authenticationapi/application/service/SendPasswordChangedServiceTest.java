package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.EmailSenderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendPasswordChangedServiceTest {
    @Mock EmailSenderPort sender;
    @Test void sendsPasswordChangedNotification() {
        new SendPasswordChangedService(sender).execute("user@example.com");
        verify(sender).send(argThat(mail -> mail.recipient().equals("user@example.com") && mail.template().equals("password-changed")));
    }
}
