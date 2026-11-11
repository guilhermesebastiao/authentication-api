package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoveryPasswordExpiredException extends DomainException {
    public RecoveryPasswordExpiredException() {
        super(
                "Token de recuperação de senha expirado",
                "O token de recuperação de senha expirou.",
                410,
                "RecoveryPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.RECOVERY_PASSWORD_EXPIRED,
                null,
                null
        );
    }
    public RecoveryPasswordExpiredException(Object details) {
        super(
                "Token de recuperação de senha expirado",
                "O token de recuperação de senha expirou.",
                410,
                "RecoveryPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.RECOVERY_PASSWORD_EXPIRED,
                details,
                null
        );
    }
    public RecoveryPasswordExpiredException(Throwable cause) {
        super(
                "Token de recuperação de senha expirado",
                "O token de recuperação de senha expirou.",
                410,
                "RecoveryPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.RECOVERY_PASSWORD_EXPIRED,
                null,
                cause
        );
    }
    public RecoveryPasswordExpiredException(Object details, Throwable cause) {
        super(
                "Token de recuperação de senha expirado",
                "O token de recuperação de senha expirou.",
                410,
                "RecoveryPasswordExpiredException: The password recovery token has expired.",
                DomainErrorCode.RECOVERY_PASSWORD_EXPIRED,
                details,
                cause
        );
    }
}
