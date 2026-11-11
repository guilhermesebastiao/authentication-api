package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class ForgotPasswordAlreadyVerifiedException extends DomainException {
    public ForgotPasswordAlreadyVerifiedException() {
        super(
                "Recuperação de senha já verificada",
                "A solicitação de recuperação de senha já foi verificada.",
                409,
                "ForgotPasswordAlreadyVerifiedException: The password recovery has already been verified.",
                DomainErrorCode.FORGOT_PASSWORD_ALREADY_VERIFIED,
                null,
                null
        );
    }
    public ForgotPasswordAlreadyVerifiedException(Object details) {
        super(
                "Recuperação de senha já verificada",
                "A solicitação de recuperação de senha já foi verificada.",
                409,
                "ForgotPasswordAlreadyVerifiedException: The password recovery has already been verified.",
                DomainErrorCode.FORGOT_PASSWORD_ALREADY_VERIFIED,
                details,
                null
        );
    }
    public ForgotPasswordAlreadyVerifiedException(Throwable cause) {
        super(
                "Recuperação de senha já verificada",
                "A solicitação de recuperação de senha já foi verificada.",
                409,
                "ForgotPasswordAlreadyVerifiedException: The password recovery has already been verified.",
                DomainErrorCode.FORGOT_PASSWORD_ALREADY_VERIFIED,
                null,
                cause
        );
    }
    public ForgotPasswordAlreadyVerifiedException(Object details, Throwable cause) {
        super(
                "Recuperação de senha já verificada",
                "A solicitação de recuperação de senha já foi verificada.",
                409,
                "ForgotPasswordAlreadyVerifiedException: The password recovery has already been verified.",
                DomainErrorCode.FORGOT_PASSWORD_ALREADY_VERIFIED,
                details,
                cause
        );
    }
}
