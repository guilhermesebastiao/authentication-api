package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoverPasswordNotVerifiedException extends DomainException {
    public RecoverPasswordNotVerifiedException() {
        super(
                DomainErrorCode.RECOVER_PASSWORD_NOT_VERIFIED,
                "The password recovery not verified",
                401
        );
    }
}
