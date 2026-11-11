package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class DisableAccountInvalidCredentialsException extends DomainException {
    public DisableAccountInvalidCredentialsException() {
        super(
                "Senha Inválida",
                "A senha informada é inválida.",
                401,
                "ChangePasswordInvalidCredentialsException: The provided password are invalid.",
                DomainErrorCode.DISABLE_ACCOUNT_INVALID_CREDENTIAL,
                null,
                null
        );
    }
    public DisableAccountInvalidCredentialsException(Object details) {
        super(
                "Senha Inválida",
                "A senha informada é inválida.",
                401,
                "ChangePasswordInvalidCredentialsException: The provided password are invalid.",
                DomainErrorCode.DISABLE_ACCOUNT_INVALID_CREDENTIAL,
                details,
                null
        );
    }
    public DisableAccountInvalidCredentialsException(Throwable cause) {
        super(
                "Senha Inválida",
                "A senha informada é inválida.",
                401,
                "ChangePasswordInvalidCredentialsException: The provided password are invalid.",
                DomainErrorCode.DISABLE_ACCOUNT_INVALID_CREDENTIAL,
                null,
                cause
        );
    }
    public DisableAccountInvalidCredentialsException(Object details, Throwable cause) {
        super(
                "Senha Inválida",
                "A senha informada é inválida.",
                401,
                "ChangePasswordInvalidCredentialsException: The provided password are invalid.",
                DomainErrorCode.DISABLE_ACCOUNT_INVALID_CREDENTIAL,
                details,
                cause
        );
    }
}
