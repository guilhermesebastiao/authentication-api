package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionNotBelongToAccountException extends DomainException {
    public SessionNotBelongToAccountException() {
        super(
                "Session Not Belong To Account",
                403,
                "The session does not belong to the requested account.",
                "SessionNotBelongToAccountException: The session does not belong to the requested account.",
                DomainErrorCode.SESSION_NOT_BELONG_TO_ACCOUNT
        );
    }
}
