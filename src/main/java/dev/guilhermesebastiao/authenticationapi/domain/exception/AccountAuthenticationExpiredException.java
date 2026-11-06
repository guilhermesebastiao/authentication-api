package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountAuthenticationExpiredException extends DomainException {
    public AccountAuthenticationExpiredException() {
        super(
                DomainErrorCode.ACCOUNT_EXPIRED,
                "Account authentication has expired.",
                401
        );
    }
}
