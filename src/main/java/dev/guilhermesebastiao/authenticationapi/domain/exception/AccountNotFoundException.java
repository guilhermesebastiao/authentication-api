package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountNotFoundException extends DomainException {
    public AccountNotFoundException() {
        super(
                DomainErrorCode.ACCOUNT_NOT_FOUND,
                "The account could not be found.",
                404
        );
    }
}
