package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionRevokedException extends DomainException {
    public SessionRevokedException() {
        super(
                "Sessão revogada",
                "A sessão foi revogada.",
                401,
                "SessionRevokedException: The session has been revoked.",
                DomainErrorCode.SESSION_REVOKED,
                null,
                null
        );
    }
    public SessionRevokedException(Object details) {
        super(
                "Sessão revogada",
                "A sessão foi revogada.",
                401,
                "SessionRevokedException: The session has been revoked.",
                DomainErrorCode.SESSION_REVOKED,
                details,
                null
        );
    }
    public SessionRevokedException(Throwable cause) {
        super(
                "Sessão revogada",
                "A sessão foi revogada.",
                401,
                "SessionRevokedException: The session has been revoked.",
                DomainErrorCode.SESSION_REVOKED,
                null,
                cause
        );
    }
    public SessionRevokedException(Object details, Throwable cause) {
        super(
                "Sessão revogada",
                "A sessão foi revogada.",
                401,
                "SessionRevokedException: The session has been revoked.",
                DomainErrorCode.SESSION_REVOKED,
                details,
                cause
        );
    }
}
