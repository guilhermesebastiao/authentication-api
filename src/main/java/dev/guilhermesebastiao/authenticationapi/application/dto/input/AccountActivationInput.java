package dev.guilhermesebastiao.authenticationapi.application.dto.input;

public record AccountActivationInput(
        String activationToken,
        String otp
) {
}
