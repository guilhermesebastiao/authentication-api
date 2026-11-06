package dev.guilhermesebastiao.authenticationapi.application.dto.input;

public record CreateAccountInput(
       String email,
       String password
) {
}
