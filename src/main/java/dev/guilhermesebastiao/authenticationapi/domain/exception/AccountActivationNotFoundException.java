package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountActivationNotFoundException extends DomainException {
    public AccountActivationNotFoundException() {
        super(
                "Ativação de conta não encontrada",
                "O registro de ativação da conta não foi encontrado.",
                404,
                "AccountActivationNotFoundException: The account activation record could not be found.",
                DomainErrorCode.ACCOUNT_ACTIVATION_NOT_FOUND,
                null,
                null
        );
    }
    public AccountActivationNotFoundException(Object details) {
        super(
                "Ativação de conta não encontrada",
                "O registro de ativação da conta não foi encontrado.",
                404,
                "AccountActivationNotFoundException: The account activation record could not be found.",
                DomainErrorCode.ACCOUNT_ACTIVATION_NOT_FOUND,
                details,
                null
        );
    }
    public AccountActivationNotFoundException(Throwable cause) {
        super(
                "Ativação de conta não encontrada",
                "O registro de ativação da conta não foi encontrado.",
                404,
                "AccountActivationNotFoundException: The account activation record could not be found.",
                DomainErrorCode.ACCOUNT_ACTIVATION_NOT_FOUND,
                null,
                cause
        );
    }
    public AccountActivationNotFoundException(Object details, Throwable cause) {
        super(
                "Ativação de conta não encontrada",
                "O registro de ativação da conta não foi encontrado.",
                404,
                "AccountActivationNotFoundException: The account activation record could not be found.",
                DomainErrorCode.ACCOUNT_ACTIVATION_NOT_FOUND,
                details,
                cause
        );
    }
}
