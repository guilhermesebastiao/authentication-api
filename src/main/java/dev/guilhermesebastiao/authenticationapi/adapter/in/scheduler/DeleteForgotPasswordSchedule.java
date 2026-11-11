package dev.guilhermesebastiao.authenticationapi.adapter.in.scheduler;

import dev.guilhermesebastiao.authenticationapi.application.port.in.DeleteForgotPasswordScheduleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteForgotPasswordSchedule {
    private final DeleteForgotPasswordScheduleUseCase deleteForgotPasswordScheduleUseCase;

    @Scheduled(fixedDelayString = "${app.scheduler.forgot-password-delay}")
    public void execute() {
        deleteForgotPasswordScheduleUseCase.execute();
    }
}
