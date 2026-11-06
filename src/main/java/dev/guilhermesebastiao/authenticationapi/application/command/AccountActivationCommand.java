package dev.guilhermesebastiao.authenticationapi.application.command;

public record AccountActivationCommand(
        String activationToken,
        String otpCode
) {
}
