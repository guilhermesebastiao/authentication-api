package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountActivationNotFoundException extends DomainException {
    public AccountActivationNotFoundException() {
        super(
                "Account Activation Not Found",
                404,
                "The account activation record could not be found.",
                "AccountActivationNotFoundException: The account activation record could not be found.",
                DomainErrorCode.ACCOUNT_ACTIVATION_NOT_FOUND
        );
    }
}
