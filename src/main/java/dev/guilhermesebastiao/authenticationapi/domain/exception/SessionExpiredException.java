package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionExpiredException extends DomainException {
    public SessionExpiredException() {
        super(
                "Sessão expirada",
                "A sessão expirou.",
                401,
                "SessionExpiredException: The session has expired.",
                DomainErrorCode.SESSION_EXPIRED,
                null,
                null
        );
    }
    public SessionExpiredException(Object details) {
        super(
                "Sessão expirada",
                "A sessão expirou.",
                401,
                "SessionExpiredException: The session has expired.",
                DomainErrorCode.SESSION_EXPIRED,
                details,
                null
        );
    }
    public SessionExpiredException(Throwable cause) {
        super(
                "Sessão expirada",
                "A sessão expirou.",
                401,
                "SessionExpiredException: The session has expired.",
                DomainErrorCode.SESSION_EXPIRED,
                null,
                cause
        );
    }
    public SessionExpiredException(Object details, Throwable cause) {
        super(
                "Sessão expirada",
                "A sessão expirou.",
                401,
                "SessionExpiredException: The session has expired.",
                DomainErrorCode.SESSION_EXPIRED,
                details,
                cause
        );
    }
}
