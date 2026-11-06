package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class PasswordSameAsCurrentException extends DomainException {
    public PasswordSameAsCurrentException() {
        super(
                DomainErrorCode.SAME_PASSWORD,
                "The new password matches the current password.",
                401
        );
    }
}
