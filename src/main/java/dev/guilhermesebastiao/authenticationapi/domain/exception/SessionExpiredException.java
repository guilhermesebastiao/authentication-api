package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionExpiredException extends DomainException {
    public SessionExpiredException() {
        super(
                "Session Expired",
                401,
                "The session has expired.",
                "SessionExpiredException: The session has expired.",
                DomainErrorCode.SESSION_EXPIRED
        );
    }
}
