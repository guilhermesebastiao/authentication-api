package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountActivationExpiredException extends DomainException {
    public AccountActivationExpiredException() {
        super(
                DomainErrorCode.ACCOUNT_ACTIVATION_EXPIRED,
                "The account activation token has expired.",
                410
        );
    }
}
