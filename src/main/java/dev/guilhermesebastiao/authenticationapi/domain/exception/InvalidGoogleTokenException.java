package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class InvalidGoogleTokenException extends DomainException {
    public InvalidGoogleTokenException() {
        super(
                "Invalid Google Token",
                400,
                "The Google identity token is invalid.",
                "InvalidGoogleTokenException: The Google identity token is invalid.",
                DomainErrorCode.GOOGLE_TOKEN_INVALID
        );
    }
}
