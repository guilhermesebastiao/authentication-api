package dev.guilhermesebastiao.authenticationapi.adapter.in.scheduler;

import dev.guilhermesebastiao.authenticationapi.application.port.in.CleanupAccountActivationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountActivationCleanupScheduler {
    private final CleanupAccountActivationUseCase cleanupAccountActivationUseCase;

    @Scheduled(fixedDelayString = "${app.scheduler.account-activation-cleanup-rate}")
    public void cleanup() {
        cleanupAccountActivationUseCase.execute();
    }
}
