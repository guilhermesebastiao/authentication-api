package dev.guilhermesebastiao.authenticationapi.application.port.in;

public interface GetDeviceNameUseCase {
    String execute(String userAgent);
}
