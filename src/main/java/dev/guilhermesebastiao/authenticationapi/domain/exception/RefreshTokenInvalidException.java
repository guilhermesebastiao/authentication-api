package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RefreshTokenInvalidException extends DomainException {
    public RefreshTokenInvalidException() {
        super(
                "Token de atualização inválido",
                "O token de atualização é inválido.",
                401,
                "RefreshTokenInvalidException: The refresh token is invalid.",
                DomainErrorCode.REFRESH_TOKEN_INVALID,
                null,
                null
        );
    }
    public RefreshTokenInvalidException(Object details) {
        super(
                "Token de atualização inválido",
                "O token de atualização é inválido.",
                401,
                "RefreshTokenInvalidException: The refresh token is invalid.",
                DomainErrorCode.REFRESH_TOKEN_INVALID,
                details,
                null
        );
    }
    public RefreshTokenInvalidException(Throwable cause) {
        super(
                "Token de atualização inválido",
                "O token de atualização é inválido.",
                401,
                "RefreshTokenInvalidException: The refresh token is invalid.",
                DomainErrorCode.REFRESH_TOKEN_INVALID,
                null,
                cause
        );
    }
    public RefreshTokenInvalidException(Object details, Throwable cause) {
        super(
                "Token de atualização inválido",
                "O token de atualização é inválido.",
                401,
                "RefreshTokenInvalidException: The refresh token is invalid.",
                DomainErrorCode.REFRESH_TOKEN_INVALID,
                details,
                cause
        );
    }
}
