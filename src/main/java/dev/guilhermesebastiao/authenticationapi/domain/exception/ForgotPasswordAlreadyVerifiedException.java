package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class ForgotPasswordAlreadyVerifiedException extends DomainException {
    public ForgotPasswordAlreadyVerifiedException() {
        super(
                "Forgot Password Already Verified",
                409,
                "The forgot password has already been verified.",
                "ForgotPasswordAlreadyVerifiedException: The password recovery has already been verified.",
                DomainErrorCode.FORGOT_PASSWORD_ALREADY_VERIFIED
        );
    }
}
