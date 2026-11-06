package dev.guilhermesebastiao.authenticationapi.domain.enums;

public enum JwtValidationStatus {
    VALID,
    EXPIRED,
    INVALID_SIGNATURE,
    INVALID_ALGORITHM,
    INVALID_ISSUER,
    INVALID
}
