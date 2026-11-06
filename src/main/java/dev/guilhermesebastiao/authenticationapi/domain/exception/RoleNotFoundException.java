package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException() {
        super(
                "Role Not Found",
                404,
                "The requested role could not be found.",
                "RoleNotFoundException: The requested role could not be found.",
                DomainErrorCode.ROLE_NOT_FOUND
        );
    }
}
