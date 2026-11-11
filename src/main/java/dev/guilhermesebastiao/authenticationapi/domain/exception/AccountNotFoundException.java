package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountNotFoundException extends DomainException {
    public AccountNotFoundException() {
        super(
                "Conta não encontrada",
                "A conta não foi encontrada.",
                404,
                "AccountNotFoundException: The account could not be found.",
                DomainErrorCode.ACCOUNT_NOT_FOUND,
                null,
                null
        );
    }
    public AccountNotFoundException(Object details) {
        super(
                "Conta não encontrada",
                "A conta não foi encontrada.",
                404,
                "AccountNotFoundException: The account could not be found.",
                DomainErrorCode.ACCOUNT_NOT_FOUND,
                details,
                null
        );
    }
    public AccountNotFoundException(Throwable cause) {
        super(
                "Conta não encontrada",
                "A conta não foi encontrada.",
                404,
                "AccountNotFoundException: The account could not be found.",
                DomainErrorCode.ACCOUNT_NOT_FOUND,
                null,
                cause
        );
    }
    public AccountNotFoundException(Object details, Throwable cause) {
        super(
                "Conta não encontrada",
                "A conta não foi encontrada.",
                404,
                "AccountNotFoundException: The account could not be found.",
                DomainErrorCode.ACCOUNT_NOT_FOUND,
                details,
                cause
        );
    }
}
