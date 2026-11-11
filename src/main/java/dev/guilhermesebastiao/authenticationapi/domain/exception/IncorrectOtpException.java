package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class IncorrectOtpException extends DomainException {
    public IncorrectOtpException() {
        super(
                "Código OTP incorreto",
                "A senha de uso único informada está incorreta.",
                401,
                "IncorrectOtpException: The provided one-time password is incorrect.",
                DomainErrorCode.INCORRECT_OTP,
                null,
                null
        );
    }
    public IncorrectOtpException(Object details) {
        super(
                "Código OTP incorreto",
                "A senha de uso único informada está incorreta.",
                401,
                "IncorrectOtpException: The provided one-time password is incorrect.",
                DomainErrorCode.INCORRECT_OTP,
                details,
                null
        );
    }
    public IncorrectOtpException(Throwable cause) {
        super(
                "Código OTP incorreto",
                "A senha de uso único informada está incorreta.",
                401,
                "IncorrectOtpException: The provided one-time password is incorrect.",
                DomainErrorCode.INCORRECT_OTP,
                null,
                cause
        );
    }
    public IncorrectOtpException(Object details, Throwable cause) {
        super(
                "Código OTP incorreto",
                "A senha de uso único informada está incorreta.",
                401,
                "IncorrectOtpException: The provided one-time password is incorrect.",
                DomainErrorCode.INCORRECT_OTP,
                details,
                cause
        );
    }
}
