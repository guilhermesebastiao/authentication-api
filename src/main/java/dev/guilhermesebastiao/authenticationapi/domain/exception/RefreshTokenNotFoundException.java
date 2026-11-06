package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RefreshTokenNotFoundException extends DomainException {
    public RefreshTokenNotFoundException() {
        super(
                DomainErrorCode.REFRESH_TOKEN_NOT_FOUND,
                "The refresh token could not be found.",
                404
        );
    }
}
