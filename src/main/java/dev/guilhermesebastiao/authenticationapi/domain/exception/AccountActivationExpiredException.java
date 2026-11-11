package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountActivationExpiredException extends DomainException {
    public AccountActivationExpiredException() {
        super(
                "Ativação de conta expirada",
                "O prazo para ativar sua conta expirou.",
                410,
                "AccountActivationExpiredException: The account activation has expired.",
                DomainErrorCode.ACCOUNT_ACTIVATION_EXPIRED,
                null
        );
    }

    public AccountActivationExpiredException(Object details) {
        super(
                "Ativação de conta expirada",
                "O prazo para ativar sua conta expirou.",
                410,
                "AccountActivationExpiredException: The account activation has expired.",
                DomainErrorCode.ACCOUNT_ACTIVATION_EXPIRED,
                details
        );
    }

    public AccountActivationExpiredException(Throwable cause) {
        super(
                "Ativação de conta expirada",
                "O prazo para ativar sua conta expirou.",
                410,
                "AccountActivationExpiredException: The account activation token has expired.",
                DomainErrorCode.ACCOUNT_ACTIVATION_EXPIRED,
                null,
                cause
        );
    }

    public AccountActivationExpiredException(Object details, Throwable cause) {
        super(
                "Ativação de conta expirada",
                "O prazo para ativar sua conta expirou.",
                410,
                "AccountActivationExpiredException: The account activation token has expired.",
                DomainErrorCode.ACCOUNT_ACTIVATION_EXPIRED,
                details,
                cause
        );
    }
}
