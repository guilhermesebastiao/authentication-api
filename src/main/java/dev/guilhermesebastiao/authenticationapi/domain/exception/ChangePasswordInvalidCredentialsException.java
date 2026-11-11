package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class ChangePasswordInvalidCredentialsException extends DomainException {
    public ChangePasswordInvalidCredentialsException() {
        super(
                "Senha Inválida",
                "A senha informada é inválida.",
                401,
                "ChangePasswordInvalidCredentialsException: The provided password are invalid.",
                DomainErrorCode.CHANGE_PASSWORD_INVALID_CREDENTIAL,
                null,
                null
        );
    }
    public ChangePasswordInvalidCredentialsException(Object details) {
        super(
                "Senha Inválida",
                "A senha informada é inválida.",
                401,
                "ChangePasswordInvalidCredentialsException: The provided password are invalid.",
                DomainErrorCode.CHANGE_PASSWORD_INVALID_CREDENTIAL,
                details,
                null
        );
    }
    public ChangePasswordInvalidCredentialsException(Throwable cause) {
        super(
                "Senha Inválida",
                "A senha informada é inválida.",
                401,
                "ChangePasswordInvalidCredentialsException: The provided password are invalid.",
                DomainErrorCode.CHANGE_PASSWORD_INVALID_CREDENTIAL,
                null,
                cause
        );
    }
    public ChangePasswordInvalidCredentialsException(Object details, Throwable cause) {
        super(
                "Senha Inválida",
                "A senha informada é inválida.",
                401,
                "ChangePasswordInvalidCredentialsException: The provided password are invalid.",
                DomainErrorCode.CHANGE_PASSWORD_INVALID_CREDENTIAL,
                details,
                cause
        );
    }
}
