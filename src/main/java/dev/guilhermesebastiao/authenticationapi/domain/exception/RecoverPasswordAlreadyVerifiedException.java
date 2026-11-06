package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoverPasswordAlreadyVerifiedException extends DomainException {
    public RecoverPasswordAlreadyVerifiedException() {
        super(
                DomainErrorCode.RECOVER_PASSWORD_ALREADY_VERIFIED,
                "The password recovery has already been verified.",
                409
        );
    }
}
