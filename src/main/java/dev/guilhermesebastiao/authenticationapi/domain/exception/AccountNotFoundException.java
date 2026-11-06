package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountNotFoundException extends DomainException {
    public AccountNotFoundException() {
        super(
                "Account Not Found",
                404,
                "The account could not be found.",
                "AccountNotFoundException: The account could not be found.",
                DomainErrorCode.ACCOUNT_NOT_FOUND
        );
    }
}
