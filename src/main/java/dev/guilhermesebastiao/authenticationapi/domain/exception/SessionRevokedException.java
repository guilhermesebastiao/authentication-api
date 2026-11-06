package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionRevokedException extends DomainException {
    public SessionRevokedException() {
        super(
                "Session Revoked",
                401,
                "The session has been revoked.",
                "SessionRevokedException: The session has been revoked.",
                DomainErrorCode.SESSION_REVOKED
        );
    }
}
