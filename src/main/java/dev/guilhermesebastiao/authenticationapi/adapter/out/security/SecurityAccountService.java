package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityAccountService implements UserDetailsService {
    private final AccountRepositoryPort accountRepositoryPort;

    @Override
    public SecurityAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepositoryPort.findByEmail(username)
                .map(SecurityAccount::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public SecurityAccount loadUserById(UUID userId) {
        return accountRepositoryPort.findById(userId)
                .map(SecurityAccount::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
