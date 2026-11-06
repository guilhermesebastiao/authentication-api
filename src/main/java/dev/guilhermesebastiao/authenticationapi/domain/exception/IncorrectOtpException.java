package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class IncorrectOtpException extends DomainException {
    public IncorrectOtpException() {
        super(
                "Incorrect Otp",
                401,
                "The provided one-time password is incorrect.",
                "IncorrectOtpException: The provided one-time password is incorrect.",
                DomainErrorCode.INCORRECT_OTP
        );
    }
}
