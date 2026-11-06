package dev.guilhermesebastiao.authenticationapi.adapter.out.logger;

public sealed interface LoggerPayload permits ErrorLoggerPayload, WarnLoggerPayload, InfoLoggerPayload, ScheduleLoggerPayload {
    String message();
}
