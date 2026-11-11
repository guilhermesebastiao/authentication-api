package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountAlreadyActivatedException extends DomainException {
    public AccountAlreadyActivatedException() {
        super(
                "Conta jé ativada",
                "A conta jé esté ativada.",
                409,
                "AccountAlreadyActivatedException: The account is already activated.",
                DomainErrorCode.ACCOUNT_ALREADY_ACTIVATED,
                null,
                null
        );
    }
    public AccountAlreadyActivatedException(Object details) {
        super(
                "Conta jé ativada",
                "A conta jé esté ativada.",
                409,
                "AccountAlreadyActivatedException: The account is already activated.",
                DomainErrorCode.ACCOUNT_ALREADY_ACTIVATED,
                details,
                null
        );
    }
    public AccountAlreadyActivatedException(Throwable cause) {
        super(
                "Conta jé ativada",
                "A conta jé esté ativada.",
                409,
                "AccountAlreadyActivatedException: The account is already activated.",
                DomainErrorCode.ACCOUNT_ALREADY_ACTIVATED,
                null,
                cause
        );
    }
    public AccountAlreadyActivatedException(Object details, Throwable cause) {
        super(
                "Conta jé ativada",
                "A conta jé esté ativada.",
                409,
                "AccountAlreadyActivatedException: The account is already activated.",
                DomainErrorCode.ACCOUNT_ALREADY_ACTIVATED,
                details,
                cause
        );
    }
}
