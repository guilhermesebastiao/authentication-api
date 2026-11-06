package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountInvalidCredentialsException extends DomainException {
    public AccountInvalidCredentialsException() {
        super(
                DomainErrorCode.ACCOUNT_INVALID_CREDENTIALS,
                "The provided account credentials are invalid.",
                401
        );
    }
}
