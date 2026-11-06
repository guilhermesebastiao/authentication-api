package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RefreshTokenInvalidException extends DomainException {
    public RefreshTokenInvalidException() {
        super(
                DomainErrorCode.REFRESH_TOKEN_INVALID,
                "The refresh token is invalid.",
                401
        );
    }
}
