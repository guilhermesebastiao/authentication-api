package dev.guilhermesebastiao.authenticationapi.application.port.out;

public interface SecureRandomGeneratorPort {
    String generate(int size);
}
