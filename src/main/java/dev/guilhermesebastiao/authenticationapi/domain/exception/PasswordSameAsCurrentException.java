package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class PasswordSameAsCurrentException extends DomainException {
    public PasswordSameAsCurrentException() {
        super(
                "Senha igual é atual",
                "A nova senha é igual é senha atual.",
                401,
                "PasswordSameAsCurrentException: The new password matches the current password.",
                DomainErrorCode.SAME_PASSWORD,
                null,
                null
        );
    }
    public PasswordSameAsCurrentException(Object details) {
        super(
                "Senha igual é atual",
                "A nova senha é igual é senha atual.",
                401,
                "PasswordSameAsCurrentException: The new password matches the current password.",
                DomainErrorCode.SAME_PASSWORD,
                details,
                null
        );
    }
    public PasswordSameAsCurrentException(Throwable cause) {
        super(
                "Senha igual é atual",
                "A nova senha é igual é senha atual.",
                401,
                "PasswordSameAsCurrentException: The new password matches the current password.",
                DomainErrorCode.SAME_PASSWORD,
                null,
                cause
        );
    }
    public PasswordSameAsCurrentException(Object details, Throwable cause) {
        super(
                "Senha igual é atual",
                "A nova senha é igual é senha atual.",
                401,
                "PasswordSameAsCurrentException: The new password matches the current password.",
                DomainErrorCode.SAME_PASSWORD,
                details,
                cause
        );
    }
}
