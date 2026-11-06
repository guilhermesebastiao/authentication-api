package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountActivationResendNotAvailableException extends DomainException {
    public AccountActivationResendNotAvailableException() {
        super(
                "Account Activation Resend Not Available",
                429,
                "Account activation email resend is not currently available.",
                "AccountActivationResendNotAvailableException: Account activation email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE
        );
    }
}
