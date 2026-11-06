package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountActivationNotFoundException extends DomainException {
    public AccountActivationNotFoundException() {
        super(
                DomainErrorCode.ACCOUNT_ACTIVATION_NOT_FOUND,
                "The account activation record could not be found.",
                404
        );
    }
}
