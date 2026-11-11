package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException() {
        super(
                "Perfil não encontrado",
                "O perfil solicitado não foi encontrado.",
                404,
                "RoleNotFoundException: The requested role could not be found.",
                DomainErrorCode.ROLE_NOT_FOUND,
                null,
                null
        );
    }
    public RoleNotFoundException(Object details) {
        super(
                "Perfil não encontrado",
                "O perfil solicitado não foi encontrado.",
                404,
                "RoleNotFoundException: The requested role could not be found.",
                DomainErrorCode.ROLE_NOT_FOUND,
                details,
                null
        );
    }
    public RoleNotFoundException(Throwable cause) {
        super(
                "Perfil não encontrado",
                "O perfil solicitado não foi encontrado.",
                404,
                "RoleNotFoundException: The requested role could not be found.",
                DomainErrorCode.ROLE_NOT_FOUND,
                null,
                cause
        );
    }
    public RoleNotFoundException(Object details, Throwable cause) {
        super(
                "Perfil não encontrado",
                "O perfil solicitado não foi encontrado.",
                404,
                "RoleNotFoundException: The requested role could not be found.",
                DomainErrorCode.ROLE_NOT_FOUND,
                details,
                cause
        );
    }
}
