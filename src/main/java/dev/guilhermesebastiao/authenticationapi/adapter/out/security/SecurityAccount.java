package dev.guilhermesebastiao.authenticationapi.adapter.out.security;


import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record SecurityAccount(Account account) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public String getPassword() {
        return account.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.getStatus() == AccountStatus.ACTIVATED;
    }

    @Override
    public boolean isEnabled() {
        return account.getDisabledAt() == null;
    }
}
