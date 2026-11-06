package dev.guilhermesebastiao.authenticationapi.application.command;

public record SignInCommand(
       String email,
       String password
) {
}
