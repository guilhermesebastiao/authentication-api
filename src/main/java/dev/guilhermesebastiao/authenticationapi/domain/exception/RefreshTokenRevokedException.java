package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RefreshTokenRevokedException extends DomainException {
    public RefreshTokenRevokedException() {
        super(
                "Refresh Token Revoked",
                401,
                "The refresh token has been revoked.",
                "RefreshTokenRevokedException: The refresh token has been revoked.",
                DomainErrorCode.REFRESH_TOKEN_REVOKED
        );
    }
}
