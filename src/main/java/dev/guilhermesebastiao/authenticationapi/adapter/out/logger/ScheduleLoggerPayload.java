package dev.guilhermesebastiao.authenticationapi.adapter.out.logger;

import java.time.Instant;

public record ScheduleLoggerPayload(
        Instant timestamp,
        String message,
        String job,
        int affectedItems
) implements LoggerPayload {
}