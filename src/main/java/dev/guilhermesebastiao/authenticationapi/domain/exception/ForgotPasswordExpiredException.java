package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class ForgotPasswordExpiredException extends DomainException {
    public ForgotPasswordExpiredException() {
        super(
                "Recuperação de senha expirada",
                "O token de recuperação de senha expirou.",
                410,
                "ForgotPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.FORGOT_PASSWORD_EXPIRED,
                null,
                null
        );
    }
    public ForgotPasswordExpiredException(Object details) {
        super(
                "Recuperação de senha expirada",
                "O token de recuperação de senha expirou.",
                410,
                "ForgotPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.FORGOT_PASSWORD_EXPIRED,
                details,
                null
        );
    }
    public ForgotPasswordExpiredException(Throwable cause) {
        super(
                "Recuperação de senha expirada",
                "O token de recuperação de senha expirou.",
                410,
                "ForgotPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.FORGOT_PASSWORD_EXPIRED,
                null,
                cause
        );
    }
    public ForgotPasswordExpiredException(Object details, Throwable cause) {
        super(
                "Recuperação de senha expirada",
                "O token de recuperação de senha expirou.",
                410,
                "ForgotPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.FORGOT_PASSWORD_EXPIRED,
                details,
                cause
        );
    }
}
