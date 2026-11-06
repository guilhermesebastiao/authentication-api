package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ResendForgotPasswordRequest(
        @NotNull
        @Length(max = 32)
        String forgotToken
) {
}
