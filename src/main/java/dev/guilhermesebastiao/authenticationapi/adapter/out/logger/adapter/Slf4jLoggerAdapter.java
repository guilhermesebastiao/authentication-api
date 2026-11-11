package dev.guilhermesebastiao.authenticationapi.adapter.out.logger.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload.*;
import dev.guilhermesebastiao.authenticationapi.application.port.out.LoggerPort;
import dev.guilhermesebastiao.authenticationapi.infrastructure.config.LoggerConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Slf4jLoggerAdapter implements LoggerPort {

    private final Logger logger;

    public Slf4jLoggerAdapter(
            @Qualifier(LoggerConfig.APPLICATION_LOGGER)
            Logger logger
    ) {
        this.logger = logger;
    }

    @Override
    public void info(LoggerPayload payload) {
        switch (payload) {
            case ScheduleLoggerPayload schedule ->
                    logSchedule(schedule);

            case InfoLoggerPayload info ->
                    logInfo(info);

            default -> throw new IllegalArgumentException(
                    "Unsupported payload for info log: " + payload.getClass().getSimpleName()
            );
        }
    }

    @Override
    public void warn(WarnLoggerPayload payload) {
        logger.warn(
                """
                ┌─ REQUEST WARNING ───────────────────────
                │ Method: {}
                │ Path: {}
                │ Status: {}
                │ IP: {}
                │ Timestamp: {}
                │ Error Code: {}
                │ Message: {}
                │ Exception: {}
                └─────────────────────────────────────────
                """,
                payload.method(),
                payload.path(),
                payload.status(),
                payload.ip(),
                payload.timestamp(),
                payload.errorCode(),
                payload.message(),
                payload.exception()
        );
    }

    @Override
    public void error(ErrorLoggerPayload payload) {
        logger.error(
                """
                ┌─ REQUEST ERROR ─────────────────────────
                │ Method: {}
                │ Path: {}
                │ Status: {}
                │ IP: {}
                │ Timestamp: {}
                │ Error Code: {}
                │ Message: {}
                │ Exception: {}
                └─────────────────────────────────────────
                """,
                payload.method(),
                payload.path(),
                payload.status(),
                payload.ip(),
                payload.timestamp(),
                payload.errorCode(),
                payload.message(),
                payload.exception()
        );
    }

    private void logInfo(InfoLoggerPayload payload) {
        logger.info(
                """
                ┌─ APPLICATION INFO ──────────────────────
                │ Operation: {}
                │ Timestamp: {}
                │ Message: {}
                └─────────────────────────────────────────
                """,
                payload.operation(),
                payload.timestamp(),
                payload.message()
        );
    }

    private void logSchedule(ScheduleLoggerPayload payload) {
        logger.info(
                """
                ┌─ SCHEDULE ──────────────────────────────
                │ Job: {}
                │ Affected Items: {}
                │ Message: {}
                └─────────────────────────────────────────
                """,
                payload.job(),
                payload.affectedItems(),
                payload.message()
        );
    }

}
