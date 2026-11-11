package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RefreshTokenRevokedException extends DomainException {
    public RefreshTokenRevokedException() {
        super(
                "Token de atualização revogado",
                "O token de atualização foi revogado.",
                401,
                "RefreshTokenRevokedException: The refresh token has been revoked.",
                DomainErrorCode.REFRESH_TOKEN_REVOKED,
                null,
                null
        );
    }
    public RefreshTokenRevokedException(Object details) {
        super(
                "Token de atualização revogado",
                "O token de atualização foi revogado.",
                401,
                "RefreshTokenRevokedException: The refresh token has been revoked.",
                DomainErrorCode.REFRESH_TOKEN_REVOKED,
                details,
                null
        );
    }
    public RefreshTokenRevokedException(Throwable cause) {
        super(
                "Token de atualização revogado",
                "O token de atualização foi revogado.",
                401,
                "RefreshTokenRevokedException: The refresh token has been revoked.",
                DomainErrorCode.REFRESH_TOKEN_REVOKED,
                null,
                cause
        );
    }
    public RefreshTokenRevokedException(Object details, Throwable cause) {
        super(
                "Token de atualização revogado",
                "O token de atualização foi revogado.",
                401,
                "RefreshTokenRevokedException: The refresh token has been revoked.",
                DomainErrorCode.REFRESH_TOKEN_REVOKED,
                details,
                cause
        );
    }
}
