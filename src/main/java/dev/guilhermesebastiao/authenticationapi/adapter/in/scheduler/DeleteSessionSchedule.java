package dev.guilhermesebastiao.authenticationapi.adapter.in.scheduler;

import dev.guilhermesebastiao.authenticationapi.application.port.in.DeleteSessionScheduleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteSessionSchedule {
    private final DeleteSessionScheduleUseCase deleteSessionScheduleUseCase;

    @Scheduled(fixedDelayString = "${app.scheduler.session-delay}")
    public void execute() {
        deleteSessionScheduleUseCase.execute();
    }
}
