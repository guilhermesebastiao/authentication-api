package dev.guilhermesebastiao.authenticationapi.domain.model;

import java.time.Instant;
import java.util.UUID;

public class RecoveryPassword {
    private UUID id;
    private ForgotPassword forgotPassword;
    private String recoveryTokenHash;
    private Instant expiresAt;
    private Instant usedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public RecoveryPassword() {
    }

    public RecoveryPassword(
            UUID id,
            ForgotPassword forgotPassword,
            String recoveryTokenHash,
            Instant expiresAt,
            Instant usedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.forgotPassword = forgotPassword;
        this.recoveryTokenHash = recoveryTokenHash;
        this.expiresAt = expiresAt;
        this.usedAt = usedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ForgotPassword getForgotPassword() {
        return forgotPassword;
    }

    public void setForgotPassword(ForgotPassword forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

    public String getRecoveryTokenHash() {
        return recoveryTokenHash;
    }

    public void setRecoveryTokenHash(String recoveryTokenHash) {
        this.recoveryTokenHash = recoveryTokenHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Instant usedAt) {
        this.usedAt = usedAt;
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
