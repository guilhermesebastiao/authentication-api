package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountAlreadyExistsException extends DomainException {
    public AccountAlreadyExistsException() {
        super(
                DomainErrorCode.ACCOUNT_ALREADY_EXISTS,
                "An account with the provided identifier already exists.",
                409
        );
    }
}
