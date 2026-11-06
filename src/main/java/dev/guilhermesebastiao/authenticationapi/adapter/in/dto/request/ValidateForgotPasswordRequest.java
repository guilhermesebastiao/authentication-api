package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record ValidateForgotPasswordRequest(
        @NotNull
        @Length(max = 32)
        String forgotToken,

        @NotBlank
        @Pattern(regexp = "^\\d{6}$")
        String otp
) {
}
