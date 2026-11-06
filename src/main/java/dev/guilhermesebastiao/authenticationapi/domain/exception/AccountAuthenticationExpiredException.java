package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountAuthenticationExpiredException extends DomainException {
    public AccountAuthenticationExpiredException() {
        super(
                "Account Authentication Expired",
                401,
                "Account authentication has expired.",
                "AccountAuthenticationExpiredException: Account authentication has expired.",
                DomainErrorCode.ACCOUNT_EXPIRED
        );
    }
}
