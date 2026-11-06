package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class DomainException extends RuntimeException {
    private final String title;
    private final int status;
    private final String detail;
    private final String logMessage;
    private final DomainErrorCode code;

    public DomainException(
            String title,
            int status,
            String detail,
            String logMessage,
            DomainErrorCode code
    ) {
        super(title);
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.logMessage = logMessage;
        this.code = code;
    }

    public DomainException(
            String title,
            int status,
            String detail,
            String logMessage,
            DomainErrorCode code,
            Throwable cause
    ) {
        super(title, cause);
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.logMessage = logMessage;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public DomainErrorCode getCode() {
        return code;
    }
}
