package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaRecoveryPasswordRepository;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper.JpaRecoveryPasswordMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoveryPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecoveryPasswordRepositoryAdapterTest {

    @Mock
    private JpaRecoveryPasswordRepository repository;

    @Mock
    private JpaRecoveryPasswordMapper mapper;

    @InjectMocks
    private RecoveryPasswordRepositoryAdapter adapter;

    @Test
    @DisplayName("Should return the mapped recover password after saving it")
    void givenRecoveryPasswordAndMappedEntities_whenSave_thenReturnSavedRecoveryPassword() {
        RecoveryPassword recoverPassword = new RecoveryPassword();
        RecoveryPasswordEntity entity = new RecoveryPasswordEntity();
        RecoveryPasswordEntity savedEntity = new RecoveryPasswordEntity();
        RecoveryPassword savedRecoveryPassword = new RecoveryPassword();

        given(mapper.toEntity(recoverPassword))
                .willReturn(entity);

        given(repository.save(entity))
                .willReturn(savedEntity);

        given(mapper.toDomain(savedEntity))
                .willReturn(savedRecoveryPassword);

        RecoveryPassword result = adapter.save(recoverPassword);

        assertSame(savedRecoveryPassword, result);
        verify(mapper).toEntity(recoverPassword);
        verify(repository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Should return the mapped recover password when the token hash exists")
    void givenExistingEntity_whenfindByRecoveryTokenHash_thenReturnMappedRecoveryPassword() {
        String recoveryTokenHash = "recover-token-hash";
        RecoveryPasswordEntity entity = new RecoveryPasswordEntity();
        RecoveryPassword recoverPassword = new RecoveryPassword();

        given(repository.findByRecoveryTokenHash(recoveryTokenHash))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(recoverPassword);

        Optional<RecoveryPassword> result = adapter.findByRecoveryTokenHash(recoveryTokenHash);

        assertTrue(result.isPresent());
        assertSame(recoverPassword, result.get());
        verify(repository).findByRecoveryTokenHash(recoveryTokenHash);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when the recover token hash does not exist")
    void givenMissingEntity_whenfindByRecoveryTokenHash_thenReturnEmptyOptional() {
        String recoveryTokenHash = "missing-recover-token-hash";

        given(repository.findByRecoveryTokenHash(recoveryTokenHash))
                .willReturn(Optional.empty());

        Optional<RecoveryPassword> result = adapter.findByRecoveryTokenHash(recoveryTokenHash);

        assertTrue(result.isEmpty());
        verify(repository).findByRecoveryTokenHash(recoveryTokenHash);
        verify(mapper, never()).toDomain(any(RecoveryPasswordEntity.class));
    }

    @Test
    @DisplayName("Should return the mapped unverified recover password when the token hash exists")
    void givenExistingEntity_whenfindByRecoveryTokenHashAndNotVerified_thenReturnMappedRecoveryPassword() {
        String recoveryTokenHash = "unverified-recover-token-hash";
        RecoveryPasswordEntity entity = new RecoveryPasswordEntity();
        RecoveryPassword recoverPassword = new RecoveryPassword();

        given(repository.findByRecoveryTokenHash(recoveryTokenHash))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(recoverPassword);

        Optional<RecoveryPassword> result = adapter.findByRecoveryTokenHash(recoveryTokenHash);

        assertTrue(result.isPresent());
        assertSame(recoverPassword, result.get());
        verify(repository).findByRecoveryTokenHash(recoveryTokenHash);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when no unverified recover password exists")
    void givenMissingEntity_whenfindByRecoveryTokenHashAndNotVerified_thenReturnEmptyOptional() {
        String recoveryTokenHash = "missing-unverified-recover-token-hash";

        given(repository.findByRecoveryTokenHash(recoveryTokenHash))
                .willReturn(Optional.empty());

        Optional<RecoveryPassword> result = adapter.findByRecoveryTokenHash(recoveryTokenHash);

        assertTrue(result.isEmpty());
        verify(repository).findByRecoveryTokenHash(recoveryTokenHash);
        verify(mapper, never()).toDomain(any(RecoveryPasswordEntity.class));
    }
}
