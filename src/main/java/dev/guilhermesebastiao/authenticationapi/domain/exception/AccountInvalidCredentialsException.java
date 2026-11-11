package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountInvalidCredentialsException extends DomainException {
    public AccountInvalidCredentialsException() {
        super(
                "Credenciais da conta inválidas",
                "As credenciais informadas são inválidas.",
                401,
                "AccountInvalidCredentialsException: The provided account credentials are invalid.",
                DomainErrorCode.ACCOUNT_INVALID_CREDENTIALS,
                null,
                null
        );
    }
    public AccountInvalidCredentialsException(Object details) {
        super(
                "Credenciais da conta inválidas",
                "As credenciais informadas são inválidas.",
                401,
                "AccountInvalidCredentialsException: The provided account credentials are invalid.",
                DomainErrorCode.ACCOUNT_INVALID_CREDENTIALS,
                details,
                null
        );
    }
    public AccountInvalidCredentialsException(Throwable cause) {
        super(
                "Credenciais da conta inválidas",
                "As credenciais informadas são inválidas.",
                401,
                "AccountInvalidCredentialsException: The provided account credentials are invalid.",
                DomainErrorCode.ACCOUNT_INVALID_CREDENTIALS,
                null,
                cause
        );
    }
    public AccountInvalidCredentialsException(Object details, Throwable cause) {
        super(
                "Credenciais da conta inválidas",
                "As credenciais informadas séo inválidas.",
                401,
                "AccountInvalidCredentialsException: The provided account credentials are invalid.",
                DomainErrorCode.ACCOUNT_INVALID_CREDENTIALS,
                details,
                cause
        );
    }
}
