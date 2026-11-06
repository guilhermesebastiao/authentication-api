package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountNotActivatedException extends DomainException {
    public AccountNotActivatedException() {
        super(
                DomainErrorCode.ACCOUNT_NOT_ACTIVATED,
                "The account has not been activated.",
                403
        );
    }

    public AccountNotActivatedException(Object details) {
        super(
                DomainErrorCode.ACCOUNT_NOT_ACTIVATED,
                "The account has not been activated.",
                403,
                details
        );
    }
}
