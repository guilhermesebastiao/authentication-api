package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.result.AccountResult;
import br.com.guisebastiao.authenticationapi.domain.model.Account;
import br.com.guisebastiao.authenticationapi.domain.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetCurrentAccountServiceTest {

    private final GetCurrentAccountService service = new GetCurrentAccountService();

    @Test
    @DisplayName("Should return the account id and normalized roles")
    void givenAccountWithRoles_whenGetCurrentAccount_thenAccountDataAndNormalizedRolesAreReturned() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);

        Role administratorRole = new Role();
        administratorRole.setName("ROLE_ADMIN");
        Role auditorRole = new Role();
        auditorRole.setName("AUDITOR");
        account.setRoles(Set.of(administratorRole, auditorRole));

        AccountResult result = service.execute(account);

        assertEquals(accountId, result.id());
        assertEquals(Set.of("ADMIN", "AUDITOR"), result.roles());
    }

    @Test
    @DisplayName("Should return an empty role set when the account has no roles")
    void givenAccountWithoutRoles_whenGetCurrentAccount_thenEmptyRolesAreReturned() {
        UUID accountId = UUID.randomUUID();
        String accountEmail = "email@email.com";

        Account account = new Account();
        account.setId(accountId);
        account.setEmail(accountEmail);
        account.setRoles(Set.of());

        AccountResult result = service.execute(account);

        assertEquals(accountId, result.id());
        assertEquals(accountEmail, result.email());
        assertEquals(Set.of(), result.roles());
    }
}
