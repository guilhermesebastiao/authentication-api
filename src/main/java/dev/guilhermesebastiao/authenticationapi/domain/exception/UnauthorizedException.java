package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException() {
        super(
                "Não autorizado",
                "é necessário autenticar-se para acessar o recurso solicitado.",
                401,
                "UnauthorizedException: Authentication is required to access the requested resource.",
                DomainErrorCode.UNAUTHORIZED,
                null,
                null
        );
    }
    public UnauthorizedException(Object details) {
        super(
                "Não autorizado",
                "é necessário autenticar-se para acessar o recurso solicitado.",
                401,
                "UnauthorizedException: Authentication is required to access the requested resource.",
                DomainErrorCode.UNAUTHORIZED,
                details,
                null
        );
    }
    public UnauthorizedException(Throwable cause) {
        super(
                "Não autorizado",
                "é necessário autenticar-se para acessar o recurso solicitado.",
                401,
                "UnauthorizedException: Authentication is required to access the requested resource.",
                DomainErrorCode.UNAUTHORIZED,
                null,
                cause
        );
    }
    public UnauthorizedException(Object details, Throwable cause) {
        super(
                "Não autorizado",
                "é necessário autenticar-se para acessar o recurso solicitado.",
                401,
                "UnauthorizedException: Authentication is required to access the requested resource.",
                DomainErrorCode.UNAUTHORIZED,
                details,
                cause
        );
    }
}
