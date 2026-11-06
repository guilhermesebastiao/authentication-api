package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RefreshTokenInvalidException extends DomainException {
    public RefreshTokenInvalidException() {
        super(
                "Refresh Token Invalid",
                401,
                "The refresh token is invalid.",
                "RefreshTokenInvalidException: The refresh token is invalid.",
                DomainErrorCode.REFRESH_TOKEN_INVALID
        );
    }
}
