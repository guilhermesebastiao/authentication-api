package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionNotFoundException extends DomainException {
    public SessionNotFoundException() {
        super(
                DomainErrorCode.SESSION_NOT_FOUND,
                "The session could not be found.",
                404
        );
    }
}
