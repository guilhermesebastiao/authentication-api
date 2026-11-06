package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class PasswordSameAsCurrentException extends DomainException {
    public PasswordSameAsCurrentException() {
        super(
                "Password Same As Current",
                401,
                "The new password matches the current password.",
                "PasswordSameAsCurrentException: The new password matches the current password.",
                DomainErrorCode.SAME_PASSWORD
        );
    }
}
