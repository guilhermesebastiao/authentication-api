package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RefreshTokenRequest(
        @NotBlank
        String accessToken,

        @NotBlank
        @Length(max = 32)
        String refreshToken,

        @NotBlank
        @Length(max = 32)
        String sessionToken
) {
}
