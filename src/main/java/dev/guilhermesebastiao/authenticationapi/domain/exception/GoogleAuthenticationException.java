package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class GoogleAuthenticationException extends DomainException {
    public GoogleAuthenticationException(Throwable cause) {
        super(
                "Google Authentication",
                502,
                "Authentication with Google failed.",
                "GoogleAuthenticationException: Authentication with Google failed.",
                DomainErrorCode.GOOGLE_AUTHENTICATION_FAILED,
                cause
        );
    }
}
