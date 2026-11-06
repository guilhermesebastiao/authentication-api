package dev.guilhermesebastiao.authenticationapi.application.command;

public record CreateAccountCommand(
       String email,
       String password
) {
}
