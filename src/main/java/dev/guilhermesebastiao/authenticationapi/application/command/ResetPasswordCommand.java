package dev.guilhermesebastiao.authenticationapi.application.command;

public record ResetPasswordCommand(
        String recoverToken,
        String newPassword,
        String confirmPassword
) {
}
