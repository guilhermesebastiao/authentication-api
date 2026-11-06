package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class DomainException extends RuntimeException {
    private final int value;
    private final DomainErrorCode code;
    private final Object details;

    public DomainException(DomainErrorCode code, String message, int value) {
        super(message);
        this.code = code;
        this.value = value;
        this.details = null;
    }

    public DomainException(DomainErrorCode code, String message, int value, Object details) {
        super(message);
        this.code = code;
        this.value = value;
        this.details = details;
    }

    public DomainException(DomainErrorCode code, String message, int value, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.value = value;
        this.details = null;
    }

    public DomainException(DomainErrorCode code, String message, int value, Object details, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.value = value;
        this.details = details;
    }


    public DomainErrorCode getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }

    public Object getDetails() {
        return details;
    }
}
