package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RefreshTokenNotFoundException extends DomainException {
    public RefreshTokenNotFoundException() {
        super(
                "Refresh Token Not Found",
                404,
                "The refresh token could not be found.",
                "RefreshTokenNotFoundException: The refresh token could not be found.",
                DomainErrorCode.REFRESH_TOKEN_NOT_FOUND
        );
    }
}
