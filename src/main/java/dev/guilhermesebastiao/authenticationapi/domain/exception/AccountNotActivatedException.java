package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountNotActivatedException extends DomainException {
    public AccountNotActivatedException() {
        super(
                "Account Not Activated",
                403,
                "The account has not been activated.",
                "AccountNotActivatedException: The account has not been activated.",
                DomainErrorCode.ACCOUNT_NOT_ACTIVATED
        );
    }

    public AccountNotActivatedException(Object details) {
        super(
                "Account Not Activated",
                403,
                "The account has not been activated.",
                "AccountNotActivatedException: The account has not been activated.",
                DomainErrorCode.ACCOUNT_NOT_ACTIVATED
        );
    }
}
