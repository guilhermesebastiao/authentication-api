package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload.ScheduleLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.DeleteSessionScheduleUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.LoggerPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;

import java.time.Instant;

public class DeleteSessionScheduleService implements DeleteSessionScheduleUseCase {
    private final static String JOB_NAME = "DELETE_SESSIONS";

    private final SessionRepositoryPort sessionRepository;
    private final LoggerPort logger;

    public DeleteSessionScheduleService(
            SessionRepositoryPort sessionRepository,
            LoggerPort logger
    ) {
        this.sessionRepository = sessionRepository;
        this.logger = logger;
    }

    @Override
    public void execute() {
        Instant now = Instant.now();

        long affectedItems = sessionRepository.deleteAllExpiredOrUsed(now);

        ScheduleLoggerPayload payload = new ScheduleLoggerPayload(
                Instant.now(),
                "Sessions cleanup completed successfully.",
                JOB_NAME,
                affectedItems
        );

        logger.info(payload);
    }
}
