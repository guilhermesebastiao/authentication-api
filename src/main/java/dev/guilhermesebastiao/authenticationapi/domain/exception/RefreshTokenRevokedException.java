package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RefreshTokenRevokedException extends DomainException {
    public RefreshTokenRevokedException() {
        super(
                DomainErrorCode.REFRESH_TOKEN_REVOKED,
                "The refresh token has been revoked.",
                401
        );
    }
}
