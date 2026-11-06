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
@Table(name = "account_activations")
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AccountActivationEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "activation_token_hash")
    private String activationTokenHash;

    @Column(name = "otp_code_hash")
    private String otpCodeHash;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "resend_available_at")
    private Instant resendAvailableAt;

    @Column(name = "activated_at")
    private Instant activatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

}
