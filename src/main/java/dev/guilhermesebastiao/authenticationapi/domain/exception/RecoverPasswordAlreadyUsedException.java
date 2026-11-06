package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoverPasswordAlreadyUsedException extends DomainException {
    public RecoverPasswordAlreadyUsedException() {
        super(
                DomainErrorCode.RECOVER_PASSWORD_ALREADY_USED,
                "The password recovery token has already been used.",
                409
        );
    }
}
