package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.ErrorLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.WarnLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.LoggerPayload;

public interface LoggerPort {
    void info(LoggerPayload payload);

    void warn(WarnLoggerPayload payload);

    void error(ErrorLoggerPayload payload);
}
