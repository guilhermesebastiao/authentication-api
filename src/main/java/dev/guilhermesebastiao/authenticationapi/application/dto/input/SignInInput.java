package dev.guilhermesebastiao.authenticationapi.application.dto.input;

public record SignInInput(
       String email,
       String password
) {
}
