package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoverPasswordResendNotAvailableException extends DomainException {
    public RecoverPasswordResendNotAvailableException() {
        super(
                DomainErrorCode.RESEND_NOT_AVAILABLE,
                "Password recovery email resend is not currently available.",
                429
        );
    }
}
