package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoveryPasswordAlreadyUsedException extends DomainException {
    public RecoveryPasswordAlreadyUsedException() {
        super(
                "Recovery Password Already Used",
                409,
                "The password recovery token has already been used.",
                "RecoveryPasswordAlreadyUsedException: The password recovery token has already been used.",
                DomainErrorCode.RECOVERY_PASSWORD_ALREADY_USED
        );
    }
}
