package dev.guilhermesebastiao.authenticationapi.adapter.in.scheduler;

import dev.guilhermesebastiao.authenticationapi.application.port.in.DeleteAccountActivationScheduleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAccountActivationSchedule {
    private final DeleteAccountActivationScheduleUseCase deleteAccountActivationScheduleUseCase;

    @Scheduled(fixedDelayString = "${app.scheduler.account-activation-delay}")
    public void execute() {
        deleteAccountActivationScheduleUseCase.execute();
    }
}
