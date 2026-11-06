package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ForgotPasswordRequest(
        @NotBlank
        @Email
        @Length(max = 255)
        String email
) {
}
