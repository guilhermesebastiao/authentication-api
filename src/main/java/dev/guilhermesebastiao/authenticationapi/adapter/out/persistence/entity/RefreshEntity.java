package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "refreshes")
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RefreshEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id")
    private SessionEntity session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replaced_by")
    private RefreshEntity replacedBy;

    @OneToMany(mappedBy = "replacedBy", fetch = FetchType.LAZY)
    private List<RefreshEntity> children;

    @Column(name = "refresh_token_hash")
    private String refreshTokenHash;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
