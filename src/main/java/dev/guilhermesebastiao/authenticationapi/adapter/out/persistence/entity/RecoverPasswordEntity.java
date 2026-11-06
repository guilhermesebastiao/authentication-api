package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "recover_passwords")
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RecoverPasswordEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "recover_token_hash")
    private String recoverTokenHash;

    @Column(name = "opt_code_hash")
    private String otpCodeHash;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "resend_available_at")
    private Instant resendAvailableAt;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
