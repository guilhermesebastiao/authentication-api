package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class ForgotPasswordExpiredException extends DomainException {
    public ForgotPasswordExpiredException() {
        super(
                "Forgot Password Expired",
                410,
                "The forgot password token has expired.",
                "ForgotPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.FORGOT_PASSWORD_EXPIRED
        );
    }
}
