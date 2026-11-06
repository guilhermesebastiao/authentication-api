package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.valid_password.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record DisableAccountRequest(
        @NotBlank
        @Length(min = 6)
        @Length(max = 20)
        @ValidPassword
        String password
) {
}
