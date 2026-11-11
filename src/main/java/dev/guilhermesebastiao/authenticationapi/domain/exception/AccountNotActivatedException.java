package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountNotActivatedException extends DomainException {
    public AccountNotActivatedException() {
        super(
                "Conta não ativada",
                "A conta ainda não foi ativada.",
                403,
                "AccountNotActivatedException: The account has not been activated.",
                DomainErrorCode.ACCOUNT_NOT_ACTIVATED,
                null,
                null
        );
    }
    public AccountNotActivatedException(Object details) {
        super(
                "Conta não ativada",
                "A conta ainda não foi ativada.",
                403,
                "AccountNotActivatedException: The account has not been activated.",
                DomainErrorCode.ACCOUNT_NOT_ACTIVATED,
                details,
                null
        );
    }
    public AccountNotActivatedException(Throwable cause) {
        super(
                "Conta não ativada",
                "A conta ainda não foi ativada.",
                403,
                "AccountNotActivatedException: The account has not been activated.",
                DomainErrorCode.ACCOUNT_NOT_ACTIVATED,
                null,
                cause
        );
    }
    public AccountNotActivatedException(Object details, Throwable cause) {
        super(
                "Conta não ativada",
                "A conta ainda não foi ativada.",
                403,
                "AccountNotActivatedException: The account has not been activated.",
                DomainErrorCode.ACCOUNT_NOT_ACTIVATED,
                details,
                cause
        );
    }
}
