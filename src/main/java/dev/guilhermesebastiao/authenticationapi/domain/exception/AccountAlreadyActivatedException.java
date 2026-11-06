package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountAlreadyActivatedException extends DomainException {
    public AccountAlreadyActivatedException() {
        super(
                "Account Already Activated",
                409,
                "The account is already activated.",
                "AccountAlreadyActivatedException: The account is already activated.",
                DomainErrorCode.ACCOUNT_ALREADY_ACTIVATED
        );
    }
}
