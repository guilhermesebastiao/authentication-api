package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.ScheduleLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.LoggerPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CleanupAccountActivationServiceTest {

    @Mock
    private AccountActivationRepositoryPort accountActivationRepository;

    @Mock
    private LoggerPort logger;

    @InjectMocks
    private CleanupAccountActivationService service;

    @Test
    @DisplayName("Should delete expired account activations and log the affected item count")
    void givenExpiredAccountActivations_whenCleanup_thenActivationsAreDeletedAndCleanupIsLogged() {
        int affectedItems = 2;
        Instant beforeExecution = Instant.now();

        given(accountActivationRepository.deleteAllExpired(any(Instant.class)))
                .willReturn(affectedItems);

        service.execute();
        Instant afterExecution = Instant.now();

        ArgumentCaptor<Instant> deletionTimeCaptor = ArgumentCaptor.forClass(Instant.class);
        then(accountActivationRepository).should().deleteAllExpired(deletionTimeCaptor.capture());

        ArgumentCaptor<ScheduleLoggerPayload> payloadCaptor = ArgumentCaptor.forClass(ScheduleLoggerPayload.class);
        then(logger).should().info(payloadCaptor.capture());

        ScheduleLoggerPayload payload = payloadCaptor.getValue();
        assertFalse(deletionTimeCaptor.getValue().isBefore(beforeExecution));
        assertFalse(deletionTimeCaptor.getValue().isAfter(afterExecution));
        assertFalse(payload.timestamp().isBefore(beforeExecution));
        assertFalse(payload.timestamp().isAfter(afterExecution));
        assertEquals("Expired account activations cleanup completed successfully", payload.message());
        assertEquals("CLEANUP_ACCOUNT_ACTIVATION", payload.job());
        assertEquals(affectedItems, payload.affectedItems());
    }
}
