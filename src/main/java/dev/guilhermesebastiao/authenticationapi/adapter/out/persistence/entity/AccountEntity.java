package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity;

import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "accounts")
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    @OneToMany(
            mappedBy = "account",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<AccountActivationEntity> activations;

    @OneToMany(
            mappedBy = "account",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<RecoverPasswordEntity> recovers;

    @OneToMany(
            mappedBy = "account",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<SessionEntity> sessions;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AccountStatus status;

    @Column(name = "disabled_at")
    private Instant disabledAt;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
