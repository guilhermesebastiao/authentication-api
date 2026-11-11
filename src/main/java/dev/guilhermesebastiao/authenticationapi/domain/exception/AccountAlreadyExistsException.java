package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountAlreadyExistsException extends DomainException {
    public AccountAlreadyExistsException() {
        super(
                "Conta jé existente",
                "Jé existe uma conta com o identificador informado.",
                409,
                "AccountAlreadyExistsException: An account with the provided identifier already exists.",
                DomainErrorCode.ACCOUNT_ALREADY_EXISTS,
                null,
                null
        );
    }
    public AccountAlreadyExistsException(Object details) {
        super(
                "Conta jé existente",
                "Jé existe uma conta com o identificador informado.",
                409,
                "AccountAlreadyExistsException: An account with the provided identifier already exists.",
                DomainErrorCode.ACCOUNT_ALREADY_EXISTS,
                details,
                null
        );
    }
    public AccountAlreadyExistsException(Throwable cause) {
        super(
                "Conta jé existente",
                "Jé existe uma conta com o identificador informado.",
                409,
                "AccountAlreadyExistsException: An account with the provided identifier already exists.",
                DomainErrorCode.ACCOUNT_ALREADY_EXISTS,
                null,
                cause
        );
    }
    public AccountAlreadyExistsException(Object details, Throwable cause) {
        super(
                "Conta jé existente",
                "Jé existe uma conta com o identificador informado.",
                409,
                "AccountAlreadyExistsException: An account with the provided identifier already exists.",
                DomainErrorCode.ACCOUNT_ALREADY_EXISTS,
                details,
                cause
        );
    }
}
