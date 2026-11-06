package dev.guilhermesebastiao.authenticationapi.domain.model;

import java.time.Instant;
import java.util.UUID;

public class AccountActivation {
    private UUID id;
    private Account account;
    private String activationTokenHash;
    private String otpCodeHash;
    private Instant expiresAt;
    private Instant resendAvailableAt;
    private Instant activatedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public AccountActivation() {}

    public AccountActivation(
            UUID id,
            Account account,
            String activationTokenHash,
            String otpCodeHash,
            Instant expiresAt,
            Instant resendAvailableAt,
            Instant activatedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.account = account;
        this.activationTokenHash = activationTokenHash;
        this.otpCodeHash = otpCodeHash;
        this.expiresAt = expiresAt;
        this.resendAvailableAt = resendAvailableAt;
        this.activatedAt = activatedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getActivationTokenHash() {
        return activationTokenHash;
    }

    public void setActivationTokenHash(String activationTokenHash) {
        this.activationTokenHash = activationTokenHash;
    }

    public String getOtpCodeHash() {
        return otpCodeHash;
    }

    public void setOtpCodeHash(String otpCodeHash) {
        this.otpCodeHash = otpCodeHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getResendAvailableAt() {
        return resendAvailableAt;
    }

    public void setResendAvailableAt(Instant resendAvailableAt) {
        this.resendAvailableAt = resendAvailableAt;
    }

    public Instant getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(Instant activatedAt) {
        this.activatedAt = activatedAt;
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
