package dev.guilhermesebastiao.authenticationapi.application.dto.input;

public record ValidateForgotPasswordInput(
        String forgotToken,
        String otp
) {
}
