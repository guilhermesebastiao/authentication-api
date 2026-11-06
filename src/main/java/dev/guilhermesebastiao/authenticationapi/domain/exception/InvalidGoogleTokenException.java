package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class InvalidGoogleTokenException extends DomainException {
    public InvalidGoogleTokenException() {
        super(
                DomainErrorCode.GOOGLE_TOKEN_INVALID,
                "The Google identity token is invalid.",
                400
        );
    }
}
