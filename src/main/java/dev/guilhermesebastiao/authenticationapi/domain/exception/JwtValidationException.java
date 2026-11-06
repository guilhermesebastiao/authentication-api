package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;
import dev.guilhermesebastiao.authenticationapi.domain.enums.JwtValidationStatus;

public class JwtValidationException extends DomainException {
    public JwtValidationException(JwtValidationStatus status) {
        super(
                "JWT Validation Failed",
                401,
                mapErrorCode(status).message,
                "JwtValidationException: " + mapErrorCode(status).message,
                mapErrorCode(status).code
        );
    }

    private static JwtMapError mapErrorCode(JwtValidationStatus status) {
        return switch (status) {
            case EXPIRED ->
                new JwtMapError(
                        "The JWT has expired.",
                        DomainErrorCode.JWT_EXPIRED
                );

            case INVALID_SIGNATURE ->
                new JwtMapError(
                        "The JWT signature is invalid.",
                        DomainErrorCode.JWT_INVALID_SIGNATURE
                );

            case INVALID_ALGORITHM ->
                new JwtMapError(
                        "The JWT algorithm is invalid.",
                        DomainErrorCode.JWT_INVALID_ALGORITHM
                );

            case INVALID_ISSUER ->
                    new JwtMapError(
                            "The JWT issuer is invalid.",
                            DomainErrorCode.JWT_INVALID_ISSUER
                    );

            case INVALID ->
                    new JwtMapError(
                            "The JWT is invalid.",
                            DomainErrorCode.JWT_INVALID
                    );

            case VALID ->
                    throw new IllegalArgumentException("Cannot create JwtValidationException for a valid JWT");
        };
    }

    private record JwtMapError(String message, DomainErrorCode code) {}
}
