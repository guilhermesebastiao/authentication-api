package dev.guilhermesebastiao.authenticationapi.domain.model;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;

import java.time.Instant;
import java.util.UUID;

public class Session {
    private UUID id;
    private Account account;
    private String sessionTokenHash;
    private DeviceType type;
    private String userAgent;
    private String ipAddress;
    private String location;
    private Instant revokedAt;
    private Instant lastSeenAt;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;

    public Session() {}

    public Session(
            UUID id,
            Account account,
            String sessionTokenHash,
            DeviceType type,
            String userAgent,
            String ipAddress,
            String location,
            Instant revokedAt,
            Instant lastSeenAt,
            Instant expiresAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.account = account;
        this.sessionTokenHash = sessionTokenHash;
        this.type = type;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.location = location;
        this.revokedAt = revokedAt;
        this.lastSeenAt = lastSeenAt;
        this.expiresAt = expiresAt;
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

    public String getSessionTokenHash() {
        return sessionTokenHash;
    }

    public void setSessionTokenHash(String sessionTokenHash) {
        this.sessionTokenHash = sessionTokenHash;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(Instant lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
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
