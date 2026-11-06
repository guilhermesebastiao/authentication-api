package dev.guilhermesebastiao.authenticationapi.adapter.out.logger;

import java.time.Instant;

public record InfoLoggerPayload(
        Instant timestamp,
        String message,
        String operation
) implements LoggerPayload {
}