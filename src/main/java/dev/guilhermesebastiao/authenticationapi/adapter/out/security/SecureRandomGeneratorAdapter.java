package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class SecureRandomGeneratorAdapter implements SecureRandomGeneratorPort {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final SecureRandom secureRandom;

    @Override
    public String generate(int size) {
        StringBuilder code = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
}
