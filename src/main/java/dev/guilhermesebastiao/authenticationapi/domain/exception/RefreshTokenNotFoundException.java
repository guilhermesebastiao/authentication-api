package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RefreshTokenNotFoundException extends DomainException {
    public RefreshTokenNotFoundException() {
        super(
                "Token de atualização não encontrado",
                "O token de atualização não foi encontrado.",
                404,
                "RefreshTokenNotFoundException: The refresh token could not be found.",
                DomainErrorCode.REFRESH_TOKEN_NOT_FOUND,
                null,
                null
        );
    }
    public RefreshTokenNotFoundException(Object details) {
        super(
                "Token de atualização não encontrado",
                "O token de atualização não foi encontrado.",
                404,
                "RefreshTokenNotFoundException: The refresh token could not be found.",
                DomainErrorCode.REFRESH_TOKEN_NOT_FOUND,
                details,
                null
        );
    }
    public RefreshTokenNotFoundException(Throwable cause) {
        super(
                "Token de atualização não encontrado",
                "O token de atualização não foi encontrado.",
                404,
                "RefreshTokenNotFoundException: The refresh token could not be found.",
                DomainErrorCode.REFRESH_TOKEN_NOT_FOUND,
                null,
                cause
        );
    }
    public RefreshTokenNotFoundException(Object details, Throwable cause) {
        super(
                "Token de atualização não encontrado",
                "O token de atualização não foi encontrado.",
                404,
                "RefreshTokenNotFoundException: The refresh token could not be found.",
                DomainErrorCode.REFRESH_TOKEN_NOT_FOUND,
                details,
                cause
        );
    }
}
