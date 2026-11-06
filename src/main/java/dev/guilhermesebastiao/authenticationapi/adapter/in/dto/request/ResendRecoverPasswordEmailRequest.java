package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.*;

public record ResendRecoverPasswordEmailRequest(
        @NotNull(message = REQUIRED)
        @Length(max = 32, message = INVALID_MAX_SIZE)
        String recoverToken
) {
}
