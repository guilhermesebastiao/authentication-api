package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionRevokedException extends DomainException {
    public SessionRevokedException() {
        super(
                DomainErrorCode.SESSION_REVOKED,
                "The session has been revoked.",
                401
        );
    }
}
