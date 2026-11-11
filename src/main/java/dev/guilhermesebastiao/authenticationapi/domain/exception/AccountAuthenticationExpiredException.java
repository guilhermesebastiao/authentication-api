package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountAuthenticationExpiredException extends DomainException {
    public AccountAuthenticationExpiredException() {
        super(
                "Autenticação da conta expirada",
                "A autenticação da conta expirou.",
                401,
                "AccountAuthenticationExpiredException: Account authentication has expired.",
                DomainErrorCode.ACCOUNT_EXPIRED,
                null,
                null
        );
    }
    public AccountAuthenticationExpiredException(Object details) {
        super(
                "Autenticação da conta expirada",
                "A autenticação da conta expirou.",
                401,
                "AccountAuthenticationExpiredException: Account authentication has expired.",
                DomainErrorCode.ACCOUNT_EXPIRED,
                details,
                null
        );
    }
    public AccountAuthenticationExpiredException(Throwable cause) {
        super(
                "Autenticação da conta expirada",
                "A autenticação da conta expirou.",
                401,
                "AccountAuthenticationExpiredException: Account authentication has expired.",
                DomainErrorCode.ACCOUNT_EXPIRED,
                null,
                cause
        );
    }
    public AccountAuthenticationExpiredException(Object details, Throwable cause) {
        super(
                "Autenticação da conta expirada",
                "A autenticação da conta expirou.",
                401,
                "AccountAuthenticationExpiredException: Account authentication has expired.",
                DomainErrorCode.ACCOUNT_EXPIRED,
                details,
                cause
        );
    }
}
