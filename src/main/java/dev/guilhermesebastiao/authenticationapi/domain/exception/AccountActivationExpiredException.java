package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountActivationExpiredException extends DomainException {
    public AccountActivationExpiredException() {
        super(
                "Account Activation Expired",
                410,
                "The account activation token has expired.",
                "AccountActivationExpiredException: The account activation token has expired.",
                DomainErrorCode.ACCOUNT_ACTIVATION_EXPIRED
        );
    }
}
