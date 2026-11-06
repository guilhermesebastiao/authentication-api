package dev.guilhermesebastiao.authenticationapi.adapter.out.security.adapter;

import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String hash(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String rawPassword, String password) {
        return passwordEncoder.matches(rawPassword, password);
    }
}
