CREATE EXTENSION IF NOT EXISTS pgcrypto;

SET TIME ZONE 'UTC';

CREATE TYPE account_status AS ENUM (
    'PENDING',
    'ACTIVATED',
    'DISABLED'
);

CREATE TYPE device_type AS ENUM (
    'MOBILE',
    'TABLET',
    'DESKTOP',
    'OTHER'
);

CREATE TABLE accounts (
    id            UUID         NOT NULL DEFAULT gen_random_uuid(),
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    status        account_status NOT NULL DEFAULT 'PENDING',
    disabled_at   TIMESTAMPTZ,
    created_at    TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uq_accounts_email ON accounts (LOWER(email));

CREATE TABLE account_activations (
    id                    UUID         NOT NULL DEFAULT gen_random_uuid(),
    account_id            UUID         NOT NULL,
    activation_token_hash VARCHAR(255) NOT NULL,
    otp_hash              VARCHAR(255) NOT NULL,
    expires_at            TIMESTAMPTZ  NOT NULL,
    resend_available_at   TIMESTAMPTZ  NOT NULL,
    activated_at          TIMESTAMPTZ,
    created_at            TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_account_activations PRIMARY KEY (id),
    CONSTRAINT uq_account_activations_activation_token_hash UNIQUE (activation_token_hash),
    CONSTRAINT fk_account_activations_account_id FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE
);

CREATE TABLE roles (
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uq_roles_name UNIQUE (name)
);

CREATE TABLE account_roles (
    account_id UUID NOT NULL,
    role_id UUID NOT NULL,

    CONSTRAINT pk_account_roles PRIMARY KEY (account_id, role_id),
    CONSTRAINT fk_account_roles_account_id FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE,
    CONSTRAINT fk_account_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE sessions (
    id                 UUID         NOT NULL DEFAULT gen_random_uuid(),
    account_id         UUID         NOT NULL,
    session_token_hash VARCHAR(255) NOT NULL,
    type               device_type  NOT NULL,
    user_agent         TEXT         NOT NULL,
    ip_address         VARCHAR(45)  NOT NULL,
    location           VARCHAR(255),
    revoked_at         TIMESTAMPTZ,
    last_seen_at       TIMESTAMPTZ  NOT NULL,
    expires_at         TIMESTAMPTZ  NOT NULL,
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_sessions PRIMARY KEY (id),
    CONSTRAINT uq_sessions_session_token_hash UNIQUE (session_token_hash),
    CONSTRAINT fk_sessions_account_id FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE
);

CREATE INDEX idx_sessions_account_id ON sessions (account_id);
CREATE INDEX idx_sessions_account_id_session_token_hash ON sessions (account_id, session_token_hash);

CREATE TABLE refreshes (
    id                 UUID         NOT NULL DEFAULT gen_random_uuid(),
    session_id         UUID         NOT NULL,
    replaced_by        UUID,
    refresh_token_hash VARCHAR(255) NOT NULL,
    revoked_at         TIMESTAMPTZ,
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_refreshes PRIMARY KEY (id),
    CONSTRAINT uq_refreshes_refresh_token_hash UNIQUE (refresh_token_hash),
    CONSTRAINT fk_refreshes_session_id FOREIGN KEY (session_id) REFERENCES sessions (id) ON DELETE CASCADE,
    CONSTRAINT fk_refreshes_replaced_by FOREIGN KEY (replaced_by) REFERENCES refreshes (id) ON DELETE SET NULL
);

CREATE INDEX idx_refreshes_session_id ON refreshes (session_id);

CREATE TABLE forgot_passwords (
   id                  UUID         NOT NULL DEFAULT gen_random_uuid(),
   account_id          UUID         NOT NULL,
   otp_hash            VARCHAR(255) NOT NULL,
   forgot_token_hash   VARCHAR(255),
   resend_available_at TIMESTAMPTZ  NOT NULL,
   expires_at          TIMESTAMPTZ  NOT NULL,
   verified_at         TIMESTAMPTZ,
   created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
   updated_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

   CONSTRAINT pk_forgot_passwords PRIMARY KEY (id),
   CONSTRAINT uq_forgot_passwords_forgot_token_hash UNIQUE (forgot_token_hash),
   CONSTRAINT fk_forgot_passwords_account_id FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE
);

CREATE TABLE password_recovery (
   id                  UUID         NOT NULL DEFAULT gen_random_uuid(),
   forgot_password_id  UUID         NOT NULL,
   recovery_token_hash VARCHAR(255),
   expires_at          TIMESTAMPTZ  NOT NULL,
   used_at             TIMESTAMPTZ,
   created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
   updated_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

   CONSTRAINT pk_password_recovery PRIMARY KEY (id),
   CONSTRAINT uq_password_recovery_recovery_token_hash UNIQUE (recovery_token_hash),
   CONSTRAINT fk_password_recovery_forgot_password_id FOREIGN KEY (forgot_password_id) REFERENCES forgot_passwords (id) ON DELETE CASCADE
);

INSERT INTO roles (id, name)
VALUES
    (gen_random_uuid(), 'ROLE_ADMIN'),
    (gen_random_uuid(), 'ROLE_USER')
ON CONFLICT (name) DO NOTHING;
