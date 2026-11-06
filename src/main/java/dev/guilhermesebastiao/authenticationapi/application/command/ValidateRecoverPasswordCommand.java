package dev.guilhermesebastiao.authenticationapi.application.command;

public record ValidateRecoverPasswordCommand(
        String recoverToken,
        String otpCode
) {
}
