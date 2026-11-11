package dev.guilhermesebastiao.authenticationapi.application.port.in;

public interface SendAccountDisabledUseCase {
    void execute(String email);
}
