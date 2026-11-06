package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class GoogleAuthenticationException extends DomainException {
    public GoogleAuthenticationException(Throwable cause) {
        super(
                DomainErrorCode.GOOGLE_AUTHENTICATION_FAILED,
                "Authentication with Google failed.",
                502,
                cause

        );
    }
}
