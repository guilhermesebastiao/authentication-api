package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoverPasswordExpiredException extends DomainException {
    public RecoverPasswordExpiredException() {
        super(
                DomainErrorCode.RECOVER_PASSWORD_EXPIRED,
                "The password recovery token has expired.",
                410
        );
    }
}
