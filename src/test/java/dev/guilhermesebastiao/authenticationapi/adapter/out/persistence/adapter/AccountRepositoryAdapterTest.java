package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.AccountEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper.JpaAccountMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaAccountRepository;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryAdapterTest {

    @Mock
    private JpaAccountRepository repository;

    @Mock
    private JpaAccountMapper mapper;

    @InjectMocks
    private AccountRepositoryAdapter adapter;

    @Test
    @DisplayName("Should return the mapped account when the account entity exists")
    void givenExistingEntity_whenFindById_thenReturnMappedAccount() {
        UUID id = UUID.randomUUID();
        AccountEntity entity = new AccountEntity();
        Account account = new Account();

        given(repository.findById(id))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(account);

        Optional<Account> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertSame(account, result.get());
        verify(repository).findById(id);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when the account entity does not exist")
    void givenMissingEntity_whenFindById_thenReturnEmptyOptional() {
        UUID id = UUID.randomUUID();

        given(repository.findById(id))
                .willReturn(Optional.empty());

        Optional<Account> result = adapter.findById(id);

        assertTrue(result.isEmpty());
        verify(repository).findById(id);
        verify(mapper, never()).toDomain(any(AccountEntity.class));
    }

    @Test
    @DisplayName("Should return the mapped account when the account email exists")
    void givenExistingEntity_whenFindByEmail_thenReturnMappedAccount() {
        String email = "account@example.com";
        AccountEntity entity = new AccountEntity();
        Account account = new Account();

        given(repository.findByEmail(email))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(account);

        Optional<Account> result = adapter.findByEmail(email);

        assertTrue(result.isPresent());
        assertSame(account, result.get());
        verify(repository).findByEmail(email);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when the account email does not exist")
    void givenMissingEntity_whenFindByEmail_thenReturnEmptyOptional() {
        String email = "missing@example.com";

        given(repository.findByEmail(email))
                .willReturn(Optional.empty());

        Optional<Account> result = adapter.findByEmail(email);

        assertTrue(result.isEmpty());
        verify(repository).findByEmail(email);
        verify(mapper, never()).toDomain(any(AccountEntity.class));
    }

    @Test
    @DisplayName("Should return the mapped account after saving the account entity")
    void givenAccountAndMappedEntities_whenSave_thenReturnSavedAccount() {
        Account account = new Account();
        AccountEntity entity = new AccountEntity();
        AccountEntity savedEntity = new AccountEntity();
        Account savedAccount = new Account();

        given(mapper.toEntity(account))
                .willReturn(entity);

        given(repository.save(entity))
                .willReturn(savedEntity);

        given(mapper.toDomain(savedEntity))
                .willReturn(savedAccount);

        Account result = adapter.save(account);

        assertSame(savedAccount, result);
        verify(mapper).toEntity(account);
        verify(repository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }
}
