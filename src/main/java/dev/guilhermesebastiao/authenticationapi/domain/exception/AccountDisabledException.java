package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountDisabledException extends DomainException {
    public AccountDisabledException() {
        super(
                "Account Disabled",
                403,
                "The account is disabled.",
                "AccountDisabledException: The account is disabled.",
                DomainErrorCode.ACCOUNT_DISABLED
        );
    }
}
