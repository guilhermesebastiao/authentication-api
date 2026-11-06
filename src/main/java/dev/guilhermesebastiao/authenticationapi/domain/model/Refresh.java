package dev.guilhermesebastiao.authenticationapi.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Refresh {
    private UUID id;
    private Session session;
    private Refresh replacedBy;
    private String refreshTokenHash;
    private Instant revokedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public Refresh() {}

    public Refresh(
            UUID id,
            Session session,
            Refresh replacedBy,
            String refreshTokenHash,
            Instant revokedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.session = session;
        this.replacedBy = replacedBy;
        this.refreshTokenHash = refreshTokenHash;
        this.revokedAt = revokedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Refresh getReplacedBy() {
        return replacedBy;
    }

    public void setReplacedBy(Refresh replacedBy) {
        this.replacedBy = replacedBy;
    }

    public String getRefreshTokenHash() {
        return refreshTokenHash;
    }

    public void setRefreshTokenHash(String refreshTokenHash) {
        this.refreshTokenHash = refreshTokenHash;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
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
