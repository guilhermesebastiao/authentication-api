package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.*;

public record ValidateRecoverPasswordRequest(
        @NotNull(message = REQUIRED)
        @Length(max = 32, message = INVALID_MAX_SIZE)
        String recoverToken,

        @NotBlank(message = REQUIRED)
        @Pattern(regexp = "^\\d{6}$", message = INVALID_OTP)
        String otpCode
) {
}
