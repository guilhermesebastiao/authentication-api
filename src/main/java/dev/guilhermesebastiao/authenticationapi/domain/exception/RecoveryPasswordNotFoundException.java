package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RecoveryPasswordNotFoundException extends DomainException {
    public RecoveryPasswordNotFoundException() {
        super(
                "Solicitação de recuperação de senha não encontrada",
                "A solicitação de recuperação de senha não foi encontrada.",
                404,
                "RecoveryPasswordNotFoundException: The password recovery request could not be found.",
                DomainErrorCode.RECOVERY_PASSWORD_NOT_FOUND,
                null,
                null
        );
    }
    public RecoveryPasswordNotFoundException(Object details) {
        super(
                "Solicitação de recuperação de senha não encontrada",
                "A solicitação de recuperação de senha não foi encontrada.",
                404,
                "RecoveryPasswordNotFoundException: The password recovery request could not be found.",
                DomainErrorCode.RECOVERY_PASSWORD_NOT_FOUND,
                details,
                null
        );
    }
    public RecoveryPasswordNotFoundException(Throwable cause) {
        super(
                "Solicitação de recuperação de senha não encontrada",
                "A solicitação de recuperação de senha não foi encontrada.",
                404,
                "RecoveryPasswordNotFoundException: The password recovery request could not be found.",
                DomainErrorCode.RECOVERY_PASSWORD_NOT_FOUND,
                null,
                cause
        );
    }
    public RecoveryPasswordNotFoundException(Object details, Throwable cause) {
        super(
                "Solicitação de recuperação de senha não encontrada",
                "A solicitação de recuperação de senha não foi encontrada.",
                404,
                "RecoveryPasswordNotFoundException: The password recovery request could not be found.",
                DomainErrorCode.RECOVERY_PASSWORD_NOT_FOUND,
                details,
                cause
        );
    }
}
