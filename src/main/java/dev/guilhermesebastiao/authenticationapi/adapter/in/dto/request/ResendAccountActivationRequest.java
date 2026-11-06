package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ResendAccountActivationRequest(
        @NotBlank
        @Length(max = 32)
        String activationToken
) {
}
