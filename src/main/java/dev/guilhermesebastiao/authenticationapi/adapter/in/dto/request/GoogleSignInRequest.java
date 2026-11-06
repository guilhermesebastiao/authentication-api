package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.*;

public record GoogleSignInRequest(
        @NotBlank(message = REQUIRED)
        @Length(max = 2048, message = INVALID_MAX_SIZE)
        String code
) {
}
