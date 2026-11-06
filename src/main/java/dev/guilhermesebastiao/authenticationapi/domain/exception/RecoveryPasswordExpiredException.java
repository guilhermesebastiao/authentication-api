package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoveryPasswordExpiredException extends DomainException {
    public RecoveryPasswordExpiredException() {
        super(
                "Recovery Password Expired",
                410,
                "The recovery password token has expired.",
                "RecoveryPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.RECOVERY_PASSWORD_EXPIRED
        );
    }
}
