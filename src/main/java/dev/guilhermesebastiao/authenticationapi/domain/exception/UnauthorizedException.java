package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException() {
        super(
                DomainErrorCode.UNAUTHORIZED,
                "Authentication is required to access the requested resource.",
                401
        );
    }
}
