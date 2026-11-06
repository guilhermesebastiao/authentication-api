package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountAlreadyExistsException extends DomainException {
    public AccountAlreadyExistsException() {
        super(
                "Account Already Exists",
                409,
                "An account with the provided identifier already exists.",
                "AccountAlreadyExistsException: An account with the provided identifier already exists.",
                DomainErrorCode.ACCOUNT_ALREADY_EXISTS
        );
    }
}
