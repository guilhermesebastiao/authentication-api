package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.ScheduleLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CleanupAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.LoggerPort;

import java.time.Instant;

public class CleanupAccountActivationService implements CleanupAccountActivationUseCase {
    private static final String JOB_NAME = "CLEANUP_ACCOUNT_ACTIVATION";

    private final AccountActivationRepositoryPort accountActivationRepository;
    private final LoggerPort logger;

    public CleanupAccountActivationService(
            AccountActivationRepositoryPort accountActivationRepository,
            LoggerPort logger
    ) {
        this.accountActivationRepository = accountActivationRepository;
        this.logger = logger;
    }

    @Override
    public void execute() {
        Instant now = Instant.now();

        int affectedItems = accountActivationRepository.deleteAllExpired(now);

        ScheduleLoggerPayload payload = new ScheduleLoggerPayload(
                Instant.now(),
                "Expired account activations cleanup completed successfully",
                JOB_NAME,
                affectedItems
        );

        logger.info(payload);
    }
}
