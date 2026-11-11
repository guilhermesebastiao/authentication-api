package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class AccountActivationResendNotAvailableException extends DomainException {
    public AccountActivationResendNotAvailableException() {
        super(
                "Reenvio da ativação da conta indisponível",
                "O reenvio do e-mail de ativação da conta não está disponível no momento.",
                429,
                "AccountActivationResendNotAvailableException: Account activation email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE,
                null,
                null
        );
    }
    public AccountActivationResendNotAvailableException(Object details) {
        super(
                "Reenvio da ativação da conta indisponível",
                "O reenvio do e-mail de ativação da conta não está disponível no momento.",
                429,
                "AccountActivationResendNotAvailableException: Account activation email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE,
                details,
                null
        );
    }
    public AccountActivationResendNotAvailableException(Throwable cause) {
        super(
                "Reenvio da ativação da conta indisponível",
                "O reenvio do e-mail de ativação da conta não está disponível no momento.",
                429,
                "AccountActivationResendNotAvailableException: Account activation email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE,
                null,
                cause
        );
    }
    public AccountActivationResendNotAvailableException(Object details, Throwable cause) {
        super(
                "Reenvio da ativação da conta indisponível",
                "O reenvio do e-mail de ativação da conta não está disponível no momento.",
                429,
                "AccountActivationResendNotAvailableException: Account activation email resend is not currently available.",
                DomainErrorCode.RESEND_NOT_AVAILABLE,
                details,
                cause
        );
    }
}
