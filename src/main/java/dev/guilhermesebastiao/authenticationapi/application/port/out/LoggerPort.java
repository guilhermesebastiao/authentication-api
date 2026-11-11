package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload.ErrorLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload.WarnLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload.LoggerPayload;

public interface LoggerPort {
    void info(LoggerPayload payload);

    void warn(WarnLoggerPayload payload);

    void error(ErrorLoggerPayload payload);
}
