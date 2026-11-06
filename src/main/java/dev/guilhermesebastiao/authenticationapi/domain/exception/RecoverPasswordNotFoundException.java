package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoverPasswordNotFoundException extends DomainException {
    public RecoverPasswordNotFoundException() {
        super(
                DomainErrorCode.RECOVER_PASSWORD_NOT_FOUND,
                "The password recovery request could not be found.",
                404
        );
    }
}
