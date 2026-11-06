package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.AccountActivationEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper.JpaAccountActivationMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaAccountActivationRepository;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountActivationRepositoryAdapterTest {

    @Mock
    private JpaAccountActivationRepository repository;

    @Mock
    private JpaAccountActivationMapper mapper;

    @InjectMocks
    private AccountActivationRepositoryAdapter adapter;

    @Test
    @DisplayName("Should map all account activations before saving them")
    void givenAccountActivations_whenSaveAll_thenMapAndSaveAllEntities() {
        AccountActivation firstActivation = new AccountActivation();
        AccountActivation secondActivation = new AccountActivation();
        AccountActivationEntity firstEntity = new AccountActivationEntity();
        AccountActivationEntity secondEntity = new AccountActivationEntity();

        given(mapper.toEntity(firstActivation))
                .willReturn(firstEntity);

        given(mapper.toEntity(secondActivation))
                .willReturn(secondEntity);

        adapter.save(List.of(firstActivation, secondActivation));

        verify(mapper).toEntity(firstActivation);
        verify(mapper).toEntity(secondActivation);
        verify(repository).saveAll(List.of(firstEntity, secondEntity));
    }

    @Test
    @DisplayName("Should return the mapped account activation after saving it")
    void givenAccountActivationAndMappedEntities_whenSave_thenReturnSavedAccountActivation() {
        AccountActivation activation = new AccountActivation();
        AccountActivationEntity entity = new AccountActivationEntity();
        AccountActivationEntity savedEntity = new AccountActivationEntity();
        AccountActivation savedActivation = new AccountActivation();

        given(mapper.toEntity(activation))
                .willReturn(entity);

        given(repository.save(entity))
                .willReturn(savedEntity);

        given(mapper.toDomain(savedEntity))
                .willReturn(savedActivation);

        AccountActivation result = adapter.save(activation);

        assertSame(savedActivation, result);
        verify(mapper).toEntity(activation);
        verify(repository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Should return the mapped account activation when the token hash exists")
    void givenExistingEntity_whenFindByActivationTokenHash_thenReturnMappedAccountActivation() {
        String activationTokenHash = "activation-token-hash";
        AccountActivationEntity entity = new AccountActivationEntity();
        AccountActivation activation = new AccountActivation();

        given(repository.findByActivationTokenHash(activationTokenHash))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(activation);

        Optional<AccountActivation> result = adapter.findByActivationTokenHash(activationTokenHash);

        assertTrue(result.isPresent());
        assertSame(activation, result.get());
        verify(repository).findByActivationTokenHash(activationTokenHash);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when the activation token hash does not exist")
    void givenMissingEntity_whenFindByActivationTokenHash_thenReturnEmptyOptional() {
        String activationTokenHash = "missing-activation-token-hash";

        given(repository.findByActivationTokenHash(activationTokenHash))
                .willReturn(Optional.empty());

        Optional<AccountActivation> result = adapter.findByActivationTokenHash(activationTokenHash);

        assertTrue(result.isEmpty());
        verify(repository).findByActivationTokenHash(activationTokenHash);
        verify(mapper, never()).toDomain(any(AccountActivationEntity.class));
    }
}
