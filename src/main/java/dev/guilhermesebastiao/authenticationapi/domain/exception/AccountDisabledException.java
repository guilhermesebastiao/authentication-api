package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountDisabledException extends DomainException {
    public AccountDisabledException() {
        super(
                "Conta desativada",
                "Essa conta está desativada.",
                403,
                "AccountDisabledException: The account is disabled.",
                DomainErrorCode.ACCOUNT_DISABLED,
                null,
                null
        );
    }
    public AccountDisabledException(Object details) {
        super(
                "Conta desativada",
                "Essa conta está desativada.",
                403,
                "AccountDisabledException: The account is disabled.",
                DomainErrorCode.ACCOUNT_DISABLED,
                details,
                null
        );
    }
    public AccountDisabledException(Throwable cause) {
        super(
                "Conta desativada",
                "Essa conta está desativada.",
                403,
                "AccountDisabledException: The account is disabled.",
                DomainErrorCode.ACCOUNT_DISABLED,
                null,
                cause
        );
    }
    public AccountDisabledException(Object details, Throwable cause) {
        super(
                "Conta desativada",
                "Essa conta está desativada.",
                403,
                "AccountDisabledException: The account is disabled.",
                DomainErrorCode.ACCOUNT_DISABLED,
                details,
                cause
        );
    }
}
