package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class SessionNotFoundException extends DomainException {
    public SessionNotFoundException() {
        super(
                "Sessão não encontrada",
                "A sessão não foi encontrada.",
                404,
                "SessionNotFoundException: The session could not be found.",
                DomainErrorCode.SESSION_NOT_FOUND,
                null,
                null
        );
    }
    public SessionNotFoundException(Object details) {
        super(
                "Sessão não encontrada",
                "A sessão não foi encontrada.",
                404,
                "SessionNotFoundException: The session could not be found.",
                DomainErrorCode.SESSION_NOT_FOUND,
                details,
                null
        );
    }
    public SessionNotFoundException(Throwable cause) {
        super(
                "Sessão não encontrada",
                "A sessão não foi encontrada.",
                404,
                "SessionNotFoundException: The session could not be found.",
                DomainErrorCode.SESSION_NOT_FOUND,
                null,
                cause
        );
    }
    public SessionNotFoundException(Object details, Throwable cause) {
        super(
                "Sessão não encontrada",
                "A sessão não foi encontrada.",
                404,
                "SessionNotFoundException: The session could not be found.",
                DomainErrorCode.SESSION_NOT_FOUND,
                details,
                cause
        );
    }
}
