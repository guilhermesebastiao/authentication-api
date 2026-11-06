package dev.guilhermesebastiao.authenticationapi.application.command;

public record GoogleSignInCommand(
        String code
) {
}
