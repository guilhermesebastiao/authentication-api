package dev.guilhermesebastiao.authenticationapi.adapter.in.scheduler;

import dev.guilhermesebastiao.authenticationapi.application.port.in.CleanupSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionCleanupScheduler {
    private final CleanupSessionUseCase cleanupSessionUseCase;

    @Scheduled(fixedDelayString = "${app.scheduler.session-cleanup-rate}")
    public void cleanup() {
        cleanupSessionUseCase.execute();
    }
}
