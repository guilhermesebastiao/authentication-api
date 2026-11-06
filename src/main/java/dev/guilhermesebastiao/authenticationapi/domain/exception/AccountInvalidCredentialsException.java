package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountInvalidCredentialsException extends DomainException {
    public AccountInvalidCredentialsException() {
        super(
                "Account Invalid Credentials",
                401,
                "The provided account credentials are invalid.",
                "AccountInvalidCredentialsException: The provided account credentials are invalid.",
                DomainErrorCode.ACCOUNT_INVALID_CREDENTIALS
        );
    }
}
