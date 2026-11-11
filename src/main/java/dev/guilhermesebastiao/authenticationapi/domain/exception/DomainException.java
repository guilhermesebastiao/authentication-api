package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class DomainException extends RuntimeException {
    private final String title;
    private final String message;
    private final int status;
    private final DomainErrorCode code;
    private final Object details;
    private final String loggerDetails;

    public DomainException(
            String title,
            String message,
            int status,
            String loggerDetails,
            DomainErrorCode code,
            Object details
    ) {
        super(title);
        this.title = title;
        this.status = status;
        this.message = message;
        this.loggerDetails = loggerDetails;
        this.code = code;
        this.details = details;
    }

    public DomainException(
            String title,
            String message,
            int status,
            String loggerDetails,
            DomainErrorCode code,
            Object details,
            Throwable cause
    ) {
        super(title, cause);
        this.title = title;
        this.status = status;
        this.message = message;
        this.loggerDetails = loggerDetails;
        this.details = details;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public DomainErrorCode getCode() {
        return code;
    }

    public Object getDetails() {
        return details;
    }

    public String getLoggerDetails() {
        return loggerDetails;
    }
}
