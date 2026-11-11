package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionNotBelongToAccountException extends DomainException {
    public SessionNotBelongToAccountException() {
        super(
                "Sessão não pertence é conta",
                "A sessão não pertence é conta informada.",
                403,
                "SessionNotBelongToAccountException: The session does not belong to the requested account.",
                DomainErrorCode.SESSION_NOT_BELONG_TO_ACCOUNT,
                null,
                null
        );
    }
    public SessionNotBelongToAccountException(Object details) {
        super(
                "Sessão não pertence é conta",
                "A sessão não pertence é conta informada.",
                403,
                "SessionNotBelongToAccountException: The session does not belong to the requested account.",
                DomainErrorCode.SESSION_NOT_BELONG_TO_ACCOUNT,
                details,
                null
        );
    }
    public SessionNotBelongToAccountException(Throwable cause) {
        super(
                "Sessão não pertence é conta",
                "A sessão não pertence é conta informada.",
                403,
                "SessionNotBelongToAccountException: The session does not belong to the requested account.",
                DomainErrorCode.SESSION_NOT_BELONG_TO_ACCOUNT,
                null,
                cause
        );
    }
    public SessionNotBelongToAccountException(Object details, Throwable cause) {
        super(
                "Sessão não pertence é conta",
                "A sessão não pertence é conta informada.",
                403,
                "SessionNotBelongToAccountException: The session does not belong to the requested account.",
                DomainErrorCode.SESSION_NOT_BELONG_TO_ACCOUNT,
                details,
                cause
        );
    }
}
