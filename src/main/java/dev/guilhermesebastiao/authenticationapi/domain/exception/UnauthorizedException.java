package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException() {
        super(
                "Unauthorized",
                401,
                "Authentication is required to access the requested resource.",
                "UnauthorizedException: Authentication is required to access the requested resource.",
                DomainErrorCode.UNAUTHORIZED
        );
    }
}
