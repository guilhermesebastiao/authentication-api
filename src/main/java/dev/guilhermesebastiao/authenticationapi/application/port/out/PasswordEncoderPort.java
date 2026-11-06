package dev.guilhermesebastiao.authenticationapi.application.port.out;

public interface PasswordEncoderPort {
    String hash(String password);
    boolean matches(String rawPassword, String password);
}
