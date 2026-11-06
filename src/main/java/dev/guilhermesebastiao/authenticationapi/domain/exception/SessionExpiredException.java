package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionExpiredException extends DomainException {
    public SessionExpiredException() {
        super(
                DomainErrorCode.SESSION_EXPIRED,
                "The session has expired.",
                401
        );
    }
}
