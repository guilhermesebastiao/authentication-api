package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.*;

public record RefreshTokenRequest(
        @NotBlank(message = REQUIRED)
        String accessToken,

        @NotBlank(message = REQUIRED)
        @Length(max = 32, message = INVALID_MAX_SIZE)
        String refreshToken,

        @NotBlank(message = REQUIRED)
        @Length(max = 32, message = INVALID_MAX_SIZE)
        String sessionToken
) {
}
