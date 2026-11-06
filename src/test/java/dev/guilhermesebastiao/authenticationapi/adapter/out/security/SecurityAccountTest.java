package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.adapter.out.security.context.SecurityAccount;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityAccountTest {

    @Test
    @DisplayName("Should map account roles to security authorities")
    void givenAccountRoles_whenGetAuthorities_thenReturnRoleNames() {
        Account account = new Account();
        account.setRoles(Set.of(
                new Role(UUID.randomUUID(), "ROLE_ADMIN", null, null, null),
                new Role(UUID.randomUUID(), "ROLE_USER", null, null, null)
        ));
        SecurityAccount securityAccount = new SecurityAccount(account);

        Set<String> authorities = securityAccount.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());

        assertEquals(Set.of("ROLE_ADMIN", "ROLE_USER"), authorities);
    }

    @Test
    @DisplayName("Should consider an activated account unlocked")
    void givenActivatedAccount_whenCheckLocked_thenReturnFalse() {
        Account account = new Account();
        account.setStatus(AccountStatus.ACTIVATED);

        SecurityAccount securityAccount = new SecurityAccount(account);

        assertTrue(securityAccount.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should consider a pending account locked")
    void givenPendingAccount_whenCheckLocked_thenReturnTrue() {
        Account account = new Account();
        account.setStatus(AccountStatus.PENDING);

        SecurityAccount securityAccount = new SecurityAccount(account);

        assertFalse(securityAccount.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should consider a disabled-status account locked")
    void givenDisabledStatusAccount_whenCheckLocked_thenReturnTrue() {
        Account account = new Account();
        account.setStatus(AccountStatus.DISABLED);

        SecurityAccount securityAccount = new SecurityAccount(account);

        assertFalse(securityAccount.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should consider an account without disabled timestamp enabled")
    void givenAccountWithoutDisabledTimestamp_whenCheckEnabled_thenReturnTrue() {
        Account account = new Account();
        account.setDisabledAt(null);

        SecurityAccount securityAccount = new SecurityAccount(account);

        assertTrue(securityAccount.isEnabled());
    }

    @Test
    @DisplayName("Should consider an account with disabled timestamp disabled")
    void givenAccountWithDisabledTimestamp_whenCheckEnabled_thenReturnFalse() {
        Account account = new Account();
        account.setDisabledAt(Instant.parse("2026-01-01T00:00:00Z"));

        SecurityAccount securityAccount = new SecurityAccount(account);

        assertFalse(securityAccount.isEnabled());
    }
}
