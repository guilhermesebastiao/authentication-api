package dev.guilhermesebastiao.authenticationapi.application.port.in;

public interface SendPasswordChangedUseCase {
    void execute(String email);
}
