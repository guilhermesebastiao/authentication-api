package dev.guilhermesebastiao.authenticationapi.application.dto.input;

public record ChangePasswordInput(
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}
