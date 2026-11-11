package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;
import dev.guilhermesebastiao.authenticationapi.domain.enums.JwtValidationStatus;

public class JwtValidationException extends DomainException {
    public JwtValidationException(JwtValidationStatus status) {
        super(
                "Falha na validação do JWT",
                mapErrorCode(status).message,
                401,
                "JwtValidationException: " + mapErrorCode(status).loggerMessage,
                mapErrorCode(status).code,
                null,
                null
        );
    }

    private static JwtMapError mapErrorCode(JwtValidationStatus status) {
        return switch (status) {
            case EXPIRED ->
                new JwtMapError(
                        "O JWT expirou.",
                        "The JWT has expired.",
                        DomainErrorCode.JWT_EXPIRED
                );

            case INVALID_SIGNATURE ->
                new JwtMapError(
                        "A assinatura do JWT é inválida.",
                        "The JWT signature is invalid.",
                        DomainErrorCode.JWT_INVALID_SIGNATURE
                );

            case INVALID_ALGORITHM ->
                new JwtMapError(
                        "O algoritmo do JWT é inválido.",
                        "The JWT algorithm is invalid.",
                        DomainErrorCode.JWT_INVALID_ALGORITHM
                );

            case INVALID_ISSUER ->
                    new JwtMapError(
                            "O emissor do JWT é inválido.",
                            "The JWT issuer is invalid.",
                            DomainErrorCode.JWT_INVALID_ISSUER
                    );

            case INVALID ->
                    new JwtMapError(
                            "O JWT é inválido.",
                            "The JWT is invalid.",
                            DomainErrorCode.JWT_INVALID
                    );

            case VALID ->
                    throw new IllegalArgumentException("Cannot create JwtValidationException for a valid JWT");
        };
    }

    private record JwtMapError(String message, String loggerMessage, DomainErrorCode code) {}
}
