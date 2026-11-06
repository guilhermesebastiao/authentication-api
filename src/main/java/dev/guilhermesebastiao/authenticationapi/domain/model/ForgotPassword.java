package dev.guilhermesebastiao.authenticationapi.domain.model;

import java.time.Instant;
import java.util.UUID;

public class ForgotPassword {
    private UUID id;
    private Account account;
    private String otpHash;
    private String forgotTokenHash;
    private Instant resendAvailableAt;
    private Instant expiresAt;
    private Instant verifiedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public ForgotPassword() {
    }

    public ForgotPassword(
            UUID id,
            Account account,
            String otpHash,
            String forgotTokenHash,
            Instant resendAvailableAt,
            Instant expiresAt,
            Instant verifiedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.account = account;
        this.otpHash = otpHash;
        this.forgotTokenHash = forgotTokenHash;
        this.resendAvailableAt = resendAvailableAt;
        this.expiresAt = expiresAt;
        this.verifiedAt = verifiedAt;
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

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public String getForgotTokenHash() {
        return forgotTokenHash;
    }

    public void setForgotTokenHash(String forgotTokenHash) {
        this.forgotTokenHash = forgotTokenHash;
    }

    public Instant getResendAvailableAt() {
        return resendAvailableAt;
    }

    public void setResendAvailableAt(Instant resendAvailableAt) {
        this.resendAvailableAt = resendAvailableAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
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
