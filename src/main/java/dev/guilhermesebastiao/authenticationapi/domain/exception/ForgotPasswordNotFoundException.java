package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class ForgotPasswordNotFoundException extends DomainException {
    public ForgotPasswordNotFoundException() {
        super(
                "Solicitação de recuperação de senha não encontrada",
                "A solicitação de recuperação de senha não foi encontrada.",
                404,
                "ForgotPasswordNotFoundException: The password recovery request could not be found.",
                DomainErrorCode.FORGOT_PASSWORD_NOT_FOUND,
                null,
                null
        );
    }
    public ForgotPasswordNotFoundException(Object details) {
        super(
                "Solicitação de recuperação de senha não encontrada",
                "A solicitação de recuperação de senha não foi encontrada.",
                404,
                "ForgotPasswordNotFoundException: The password recovery request could not be found.",
                DomainErrorCode.FORGOT_PASSWORD_NOT_FOUND,
                details,
                null
        );
    }
    public ForgotPasswordNotFoundException(Throwable cause) {
        super(
                "Solicitação de recuperação de senha não encontrada",
                "A solicitação de recuperação de senha não foi encontrada.",
                404,
                "ForgotPasswordNotFoundException: The password recovery request could not be found.",
                DomainErrorCode.FORGOT_PASSWORD_NOT_FOUND,
                null,
                cause
        );
    }
    public ForgotPasswordNotFoundException(Object details, Throwable cause) {
        super(
                "Solicitação de recuperação de senha não encontrada",
                "A solicitação de recuperação de senha não foi encontrada.",
                404,
                "ForgotPasswordNotFoundException: The password recovery request could not be found.",
                DomainErrorCode.FORGOT_PASSWORD_NOT_FOUND,
                details,
                cause
        );
    }
}
