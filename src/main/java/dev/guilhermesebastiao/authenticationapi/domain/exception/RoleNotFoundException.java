package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException() {
        super(
                DomainErrorCode.ROLE_NOT_FOUND,
                "The requested role could not be found.",
                404
        );
    }
}
