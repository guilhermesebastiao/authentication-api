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
@Table(name = "forgot_passwords")
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ForgotPasswordEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(mappedBy = "forgotPassword")
    private RecoveryPasswordEntity passwordRecovery;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "otp_hash")
    private String otpHash;

    @Column(name = "forgot_token_hash")
    private String forgotTokenHash;

    @Column(name = "expires_at")
    private Instant expiresAt;

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
