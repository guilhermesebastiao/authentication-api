package dev.guilhermesebastiao.authenticationapi.adapter.out.logger;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

import java.time.Instant;

public record WarnLoggerPayload(
        Instant timestamp,
        String message,
        String method,
        String path,
        int status,
        String ip,
        DomainErrorCode errorCode,
        Throwable exception
) implements LoggerPayload {
}
