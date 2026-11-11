package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class ForgotPasswordResendNotAvailableException extends DomainException {
    public ForgotPasswordResendNotAvailableException() {
        super(
                "Reenvio da recuperação de senha indisponível",
                "O reenvio do e-mail de recuperação de senha não esté disponível no momento.",
                429,
                "ForgotPasswordResendNotAvailableException: Password recovery email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE,
                null,
                null
        );
    }
    public ForgotPasswordResendNotAvailableException(Object details) {
        super(
                "Reenvio da recuperação de senha indisponível",
                "O reenvio do e-mail de recuperação de senha não esté disponível no momento.",
                429,
                "ForgotPasswordResendNotAvailableException: Password recovery email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE,
                details,
                null
        );
    }
    public ForgotPasswordResendNotAvailableException(Throwable cause) {
        super(
                "Reenvio da recuperação de senha indisponível",
                "O reenvio do e-mail de recuperação de senha não esté disponível no momento.",
                429,
                "ForgotPasswordResendNotAvailableException: Password recovery email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE,
                null,
                cause
        );
    }
    public ForgotPasswordResendNotAvailableException(Object details, Throwable cause) {
        super(
                "Reenvio da recuperação de senha indisponível",
                "O reenvio do e-mail de recuperação de senha não esté disponível no momento.",
                429,
                "ForgotPasswordResendNotAvailableException: Password recovery email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE,
                details,
                cause
        );
    }
}
