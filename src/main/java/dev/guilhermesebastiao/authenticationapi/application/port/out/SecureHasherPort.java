package dev.guilhermesebastiao.authenticationapi.application.port.out;

public interface SecureHasherPort {
    String hash(String value);
}
