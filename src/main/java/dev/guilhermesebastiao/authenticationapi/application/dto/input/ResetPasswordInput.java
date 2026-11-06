package dev.guilhermesebastiao.authenticationapi.application.dto.input;

public record ResetPasswordInput(
        String recoveryToken,
        String newPassword,
        String confirmPassword
) {
}
