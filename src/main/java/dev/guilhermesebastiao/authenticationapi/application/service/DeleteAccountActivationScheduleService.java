package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload.ScheduleLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.in.DeleteAccountActivationScheduleUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.LoggerPort;

import java.time.Instant;

public class DeleteAccountActivationScheduleService implements DeleteAccountActivationScheduleUseCase {
    private final static String JOB_NAME = "DELETE_ACCOUNT_ACTIVATION";

    private final AccountActivationRepositoryPort accountActivationRepository;
    private final LoggerPort logger;

    public DeleteAccountActivationScheduleService(
            AccountActivationRepositoryPort accountActivationRepository,
            LoggerPort logger
    ) {
        this.accountActivationRepository = accountActivationRepository;
        this.logger = logger;
    }

    @Override
    public void execute() {
        Instant now = Instant.now();

        long affectedItems = accountActivationRepository.deleteAllExpiredOrUsed(now);

        ScheduleLoggerPayload payload = new ScheduleLoggerPayload(
                Instant.now(),
                "Account activation cleanup completed successfully.",
                JOB_NAME,
                affectedItems
        );

        logger.info(payload);
    }
}
