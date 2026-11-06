package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class ForgotPasswordNotFoundException extends DomainException {
    public ForgotPasswordNotFoundException() {
        super(
                "Forgot Password Not Found",
                404,
                "The forgot password request could not be found.",
                "ForgotPasswordNotFoundException: The forgot recovery request could not be found.",
                DomainErrorCode.FORGOT_PASSWORD_NOT_FOUND
        );
    }
}
