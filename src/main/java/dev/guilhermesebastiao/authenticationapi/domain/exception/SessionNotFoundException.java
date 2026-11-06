package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionNotFoundException extends DomainException {
    public SessionNotFoundException() {
        super(
                "Session Not Found",
                404,
                "The session could not be found.",
                "SessionNotFoundException: The session could not be found.",
                DomainErrorCode.SESSION_NOT_FOUND
        );
    }
}
