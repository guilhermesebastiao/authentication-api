package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class IncorrectOtpException extends DomainException {
    public IncorrectOtpException() {
        super(
                DomainErrorCode.INCORRECT_OTP,
                "The provided one-time password is incorrect.",
                401
        );
    }
}
