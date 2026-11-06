package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record GoogleSignInRequest(
        @NotBlank
        @Length(max = 2048)
        String code
) {
}
