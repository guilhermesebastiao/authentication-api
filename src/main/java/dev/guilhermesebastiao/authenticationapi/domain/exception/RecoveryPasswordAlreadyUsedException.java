package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoveryPasswordAlreadyUsedException extends DomainException {
    public RecoveryPasswordAlreadyUsedException() {
        super(
                "Token de recuperação de senha já utilizado",
                "O token de recuperação de senha já foi utilizado.",
                409,
                "RecoveryPasswordAlreadyUsedException: The password recovery token has already been used.",
                DomainErrorCode.RECOVERY_PASSWORD_ALREADY_USED,
                null,
                null
        );
    }
    public RecoveryPasswordAlreadyUsedException(Object details) {
        super(
                "Token de recuperação de senha já utilizado",
                "O token de recuperação de senha já foi utilizado.",
                409,
                "RecoveryPasswordAlreadyUsedException: The password recovery token has already been used.",
                DomainErrorCode.RECOVERY_PASSWORD_ALREADY_USED,
                details,
                null
        );
    }
    public RecoveryPasswordAlreadyUsedException(Throwable cause) {
        super(
                "Token de recuperação de senha já utilizado",
                "O token de recuperação de senha já foi utilizado.",
                409,
                "RecoveryPasswordAlreadyUsedException: The password recovery token has already been used.",
                DomainErrorCode.RECOVERY_PASSWORD_ALREADY_USED,
                null,
                cause
        );
    }
    public RecoveryPasswordAlreadyUsedException(Object details, Throwable cause) {
        super(
                "Token de recuperação de senha já utilizado",
                "O token de recuperação de senha já foi utilizado.",
                409,
                "RecoveryPasswordAlreadyUsedException: The password recovery token has already been used.",
                DomainErrorCode.RECOVERY_PASSWORD_ALREADY_USED,
                details,
                cause
        );
    }
}
