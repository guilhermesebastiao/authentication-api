package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload.ScheduleLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.DeleteForgotPasswordScheduleUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.ForgotPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.LoggerPort;

import java.time.Instant;

public class DeleteForgotPasswordScheduleService implements DeleteForgotPasswordScheduleUseCase {
    private final static String JOB_NAME = "DELETE_FORGOT_PASSWORDS";

    private final ForgotPasswordRepositoryPort forgotPasswordRepository;
    private final LoggerPort logger;

    public DeleteForgotPasswordScheduleService(
            ForgotPasswordRepositoryPort forgotPasswordRepository,
            LoggerPort logger
    ) {
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.logger = logger;
    }

    @Override
    public void execute() {
        Instant now = Instant.now();

        long affectedItems = forgotPasswordRepository.deleteAllExpiredOrUsed(now);

        ScheduleLoggerPayload payload = new ScheduleLoggerPayload(
                Instant.now(),
                "Forgot passwords cleanup completed successfully.",
                JOB_NAME,
                affectedItems
        );

        logger.info(payload);
    }
}
