package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.ScheduleLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CleanupRecoverPasswordsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.LoggerPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;

import java.time.Instant;

public class CleanupRecoverPasswordService implements CleanupRecoverPasswordsUseCase {
    private static final String JOB_NAME = "CLEANUP_RECOVER_PASSWORD";

    private final RecoverPasswordRepositoryPort recoverPasswordRepository;
    private final LoggerPort logger;

    public CleanupRecoverPasswordService(
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            LoggerPort logger
    ) {
        this.recoverPasswordRepository = recoverPasswordRepository;
        this.logger = logger;
    }

    @Override
    public void execute() {
        Instant now = Instant.now();

        int affectedItems = recoverPasswordRepository.deleteAllExpired(now);

        ScheduleLoggerPayload payload = new ScheduleLoggerPayload(
                Instant.now(),
                "Expired password recovery records cleanup completed successfully",
                JOB_NAME,
                affectedItems
        );

        logger.info(payload);
    }
}
