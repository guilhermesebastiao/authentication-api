package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class InvalidGoogleTokenException extends DomainException {
    public InvalidGoogleTokenException() {
        super(
                "Token do Google inválido",
                "O token de identidade do Google é inválido.",
                400,
                "InvalidGoogleTokenException: The Google identity token is invalid.",
                DomainErrorCode.GOOGLE_TOKEN_INVALID,
                null,
                null
        );
    }
    public InvalidGoogleTokenException(Object details) {
        super(
                "Token do Google inválido",
                "O token de identidade do Google é inválido.",
                400,
                "InvalidGoogleTokenException: The Google identity token is invalid.",
                DomainErrorCode.GOOGLE_TOKEN_INVALID,
                details,
                null
        );
    }
    public InvalidGoogleTokenException(Throwable cause) {
        super(
                "Token do Google inválido",
                "O token de identidade do Google é inválido.",
                400,
                "InvalidGoogleTokenException: The Google identity token is invalid.",
                DomainErrorCode.GOOGLE_TOKEN_INVALID,
                null,
                cause
        );
    }
    public InvalidGoogleTokenException(Object details, Throwable cause) {
        super(
                "Token do Google inválido",
                "O token de identidade do Google é inválido.",
                400,
                "InvalidGoogleTokenException: The Google identity token is invalid.",
                DomainErrorCode.GOOGLE_TOKEN_INVALID,
                details,
                cause
        );
    }
}
