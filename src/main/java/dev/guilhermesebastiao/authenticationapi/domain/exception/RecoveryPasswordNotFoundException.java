package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoveryPasswordNotFoundException extends DomainException {
    public RecoveryPasswordNotFoundException() {
        super(
                "Recovery Password Not Found",
                404,
                "The password recovery request could not be found.",
                "RecoveryPasswordNotFoundException: The password recovery request could not be found.",
                DomainErrorCode.RECOVERY_PASSWORD_NOT_FOUND
        );
    }
}
