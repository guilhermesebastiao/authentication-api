package dev.guilhermesebastiao.authenticationapi.application.command;

public record CreateRecoverPasswordCommand(
        String email
) {
}
