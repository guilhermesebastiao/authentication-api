package dev.guilhermesebastiao.authenticationapi.domain.model;

import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class Account {
    private UUID id;
    private Set<Role> roles;
    private String email;
    private String passwordHash;
    private AccountStatus status;
    private Instant disabledAt;
    private Instant createdAt;
    private Instant updatedAt;

    public Account() {}

    public Account(
            UUID id,
            Set<Role> roles,
            String email,
            String passwordHash,
            AccountStatus status,
            Instant disabledAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.roles = roles;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
        this.disabledAt = disabledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Instant getDisabledAt() {
        return disabledAt;
    }

    public void setDisabledAt(Instant disabledAt) {
        this.disabledAt = disabledAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
