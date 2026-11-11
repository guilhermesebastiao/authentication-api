package dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload;

import java.time.Instant;

public record ScheduleLoggerPayload(
        Instant timestamp,
        String message,
        String job,
        long affectedItems
) implements LoggerPayload {
}