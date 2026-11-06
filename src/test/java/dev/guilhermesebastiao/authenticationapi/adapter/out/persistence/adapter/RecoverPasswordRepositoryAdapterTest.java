package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoverPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers.JpaRecoverPasswordMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaRecoverPasswordRepository;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoverPassword;
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
class RecoverPasswordRepositoryAdapterTest {

    @Mock
    private JpaRecoverPasswordRepository repository;

    @Mock
    private JpaRecoverPasswordMapper mapper;

    @InjectMocks
    private RecoverPasswordRepositoryAdapter adapter;

    @Test
    @DisplayName("Should return the mapped recover password after saving it")
    void givenRecoverPasswordAndMappedEntities_whenSave_thenReturnSavedRecoverPassword() {
        RecoverPassword recoverPassword = new RecoverPassword();
        RecoverPasswordEntity entity = new RecoverPasswordEntity();
        RecoverPasswordEntity savedEntity = new RecoverPasswordEntity();
        RecoverPassword savedRecoverPassword = new RecoverPassword();

        given(mapper.toEntity(recoverPassword))
                .willReturn(entity);

        given(repository.save(entity))
                .willReturn(savedEntity);

        given(mapper.toDomain(savedEntity))
                .willReturn(savedRecoverPassword);

        RecoverPassword result = adapter.save(recoverPassword);

        assertSame(savedRecoverPassword, result);
        verify(mapper).toEntity(recoverPassword);
        verify(repository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Should return the mapped recover password when the token hash exists")
    void givenExistingEntity_whenFindByRecoverTokenHash_thenReturnMappedRecoverPassword() {
        String recoverTokenHash = "recover-token-hash";
        RecoverPasswordEntity entity = new RecoverPasswordEntity();
        RecoverPassword recoverPassword = new RecoverPassword();

        given(repository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(recoverPassword);

        Optional<RecoverPassword> result = adapter.findByRecoverTokenHash(recoverTokenHash);

        assertTrue(result.isPresent());
        assertSame(recoverPassword, result.get());
        verify(repository).findByRecoverTokenHash(recoverTokenHash);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when the recover token hash does not exist")
    void givenMissingEntity_whenFindByRecoverTokenHash_thenReturnEmptyOptional() {
        String recoverTokenHash = "missing-recover-token-hash";

        given(repository.findByRecoverTokenHash(recoverTokenHash))
                .willReturn(Optional.empty());

        Optional<RecoverPassword> result = adapter.findByRecoverTokenHash(recoverTokenHash);

        assertTrue(result.isEmpty());
        verify(repository).findByRecoverTokenHash(recoverTokenHash);
        verify(mapper, never()).toDomain(any(RecoverPasswordEntity.class));
    }

    @Test
    @DisplayName("Should return the mapped unverified recover password when the token hash exists")
    void givenExistingEntity_whenFindByRecoverTokenHashAndNotVerified_thenReturnMappedRecoverPassword() {
        String recoverTokenHash = "unverified-recover-token-hash";
        RecoverPasswordEntity entity = new RecoverPasswordEntity();
        RecoverPassword recoverPassword = new RecoverPassword();

        given(repository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(recoverPassword);

        Optional<RecoverPassword> result = adapter.findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        assertTrue(result.isPresent());
        assertSame(recoverPassword, result.get());
        verify(repository).findByRecoverTokenHashAndNotVerified(recoverTokenHash);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when no unverified recover password exists")
    void givenMissingEntity_whenFindByRecoverTokenHashAndNotVerified_thenReturnEmptyOptional() {
        String recoverTokenHash = "missing-unverified-recover-token-hash";

        given(repository.findByRecoverTokenHashAndNotVerified(recoverTokenHash))
                .willReturn(Optional.empty());

        Optional<RecoverPassword> result = adapter.findByRecoverTokenHashAndNotVerified(recoverTokenHash);

        assertTrue(result.isEmpty());
        verify(repository).findByRecoverTokenHashAndNotVerified(recoverTokenHash);
        verify(mapper, never()).toDomain(any(RecoverPasswordEntity.class));
    }
}
