package dev.guilhermesebastiao.authenticationapi.application.port.in;

public interface SendAccountWelcomeUseCase {
    void execute(String email);
}
