package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class GoogleAuthenticationException extends DomainException {
    public GoogleAuthenticationException() {
        super(
                "Autenticação com o Google",
                "Falha na autenticação com o Google.",
                502,
                "GoogleAuthenticationException: Authentication with Google failed.",
                DomainErrorCode.GOOGLE_AUTHENTICATION_FAILED,
                null
        );
    }

    public GoogleAuthenticationException(Object details) {
        super(
                "Autenticação com o Google",
                "Falha na autenticação com o Google.",
                502,
                "GoogleAuthenticationException: Authentication with Google failed.",
                DomainErrorCode.GOOGLE_AUTHENTICATION_FAILED,
                details
        );
    }

    public GoogleAuthenticationException(Throwable cause) {
        super(
                "Autenticação com o Google",
                "Falha na autenticação com o Google.",
                502,
                "GoogleAuthenticationException: Authentication with Google failed.",
                DomainErrorCode.GOOGLE_AUTHENTICATION_FAILED,
                null,
                cause
        );
    }

    public GoogleAuthenticationException(Object details, Throwable cause) {
        super(
                "Autenticação com o Google",
                "Falha na autenticação com o Google.",
                502,
                "GoogleAuthenticationException: Authentication with Google failed.",
                DomainErrorCode.GOOGLE_AUTHENTICATION_FAILED,
                details,
                cause
        );
    }
}
