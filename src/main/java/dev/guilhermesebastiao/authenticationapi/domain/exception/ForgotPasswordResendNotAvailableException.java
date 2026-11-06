package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class ForgotPasswordResendNotAvailableException extends DomainException {
    public ForgotPasswordResendNotAvailableException() {
        super(
                "Forgot Password Resend Not Available",
                429,
                "Forgot password email resend is not currently available.",
                "ForgotPasswordResendNotAvailableException: Password recovery email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE
        );
    }
}
