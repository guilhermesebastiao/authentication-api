package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountDisabledException extends DomainException {
    public AccountDisabledException() {
        super(
                DomainErrorCode.ACCOUNT_DISABLED,
                "The account is disabled.",
                403
        );
    }
}
