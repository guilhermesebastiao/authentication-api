package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.ScheduleLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CleanupSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.LoggerPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;

import java.time.Instant;

public class CleanupSessionService implements CleanupSessionUseCase {
    private static final String JOB_NAME = "CLEANUP_SESSION";

    private final SessionRepositoryPort sessionRepository;
    private final LoggerPort logger;

    public CleanupSessionService(
            SessionRepositoryPort sessionRepository,
            LoggerPort logger
    ) {
        this.sessionRepository = sessionRepository;
        this.logger = logger;
    }

    @Override
    public void execute() {
        Instant now = Instant.now();

        int affectedItems = sessionRepository.deleteAllRevoked(now);

        ScheduleLoggerPayload payload = new ScheduleLoggerPayload(
                Instant.now(),
                "Expired or revoked sessions records cleanup completed successfully",
                JOB_NAME,
                affectedItems
        );

        logger.info(payload);
    }
}
