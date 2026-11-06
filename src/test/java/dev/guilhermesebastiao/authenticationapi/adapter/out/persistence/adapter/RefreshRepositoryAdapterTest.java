package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RefreshEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper.JpaRefreshMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaRefreshRepository;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RefreshRepositoryAdapterTest {

    @Mock
    private JpaRefreshRepository repository;

    @Mock
    private JpaRefreshMapper mapper;

    @InjectMocks
    private RefreshRepositoryAdapter adapter;

    @Test
    @DisplayName("Should return the mapped refresh after saving it")
    void givenRefreshAndMappedEntities_whenSave_thenReturnSavedRefresh() {
        Refresh refresh = new Refresh();
        RefreshEntity entity = new RefreshEntity();
        RefreshEntity savedEntity = new RefreshEntity();
        Refresh savedRefresh = new Refresh();

        given(mapper.toEntity(refresh))
                .willReturn(entity);

        given(repository.save(entity))
                .willReturn(savedEntity);

        given(mapper.toDomain(savedEntity))
                .willReturn(savedRefresh);

        Refresh result = adapter.save(refresh);

        assertSame(savedRefresh, result);
        verify(mapper).toEntity(refresh);
        verify(repository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Should map all refreshes before saving them")
    void givenRefreshes_whenSaveAll_thenMapAndSaveAllEntities() {
        Refresh firstRefresh = new Refresh();
        Refresh secondRefresh = new Refresh();
        RefreshEntity firstEntity = new RefreshEntity();
        RefreshEntity secondEntity = new RefreshEntity();

        given(mapper.toEntity(firstRefresh))
                .willReturn(firstEntity);

        given(mapper.toEntity(secondRefresh))
                .willReturn(secondEntity);

        adapter.save(List.of(firstRefresh, secondRefresh));

        verify(mapper).toEntity(firstRefresh);
        verify(mapper).toEntity(secondRefresh);
        verify(repository).saveAll(List.of(firstEntity, secondEntity));
    }

    @Test
    @DisplayName("Should return the mapped refresh when the token hash exists")
    void givenExistingEntity_whenFindByRefreshTokenHash_thenReturnMappedRefresh() {
        String refreshTokenHash = "refresh-token-hash";
        RefreshEntity entity = new RefreshEntity();
        Refresh refresh = new Refresh();

        given(repository.findByRefreshTokenHash(refreshTokenHash))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(refresh);

        Optional<Refresh> result = adapter.findByRefreshTokenHash(refreshTokenHash);

        assertTrue(result.isPresent());
        assertSame(refresh, result.get());
        verify(repository).findByRefreshTokenHash(refreshTokenHash);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when the refresh token hash does not exist")
    void givenMissingEntity_whenFindByRefreshTokenHash_thenReturnEmptyOptional() {
        String refreshTokenHash = "missing-refresh-token-hash";

        given(repository.findByRefreshTokenHash(refreshTokenHash))
                .willReturn(Optional.empty());

        Optional<Refresh> result = adapter.findByRefreshTokenHash(refreshTokenHash);

        assertTrue(result.isEmpty());
        verify(repository).findByRefreshTokenHash(refreshTokenHash);
        verify(mapper, never()).toDomain(any(RefreshEntity.class));
    }

    @Test
    @DisplayName("Should return mapped non-revoked refreshes for the session ids")
    void givenRefreshEntities_whenFindAllBySessionIdsAndNotRevoked_thenReturnMappedRefreshes() {
        UUID firstSessionId = UUID.randomUUID();
        UUID secondSessionId = UUID.randomUUID();
        List<UUID> sessionIds = List.of(firstSessionId, secondSessionId);
        RefreshEntity firstEntity = new RefreshEntity();
        RefreshEntity secondEntity = new RefreshEntity();
        Refresh firstRefresh = new Refresh();
        Refresh secondRefresh = new Refresh();

        given(repository.findAllBySessionIdsAndNotRevoked(sessionIds))
                .willReturn(List.of(firstEntity, secondEntity));

        given(mapper.toDomain(firstEntity))
                .willReturn(firstRefresh);

        given(mapper.toDomain(secondEntity))
                .willReturn(secondRefresh);

        List<Refresh> result = adapter.findAllBySessionIdsAndNotRevoked(sessionIds);

        assertEquals(List.of(firstRefresh, secondRefresh), result);
        verify(repository).findAllBySessionIdsAndNotRevoked(sessionIds);
        verify(mapper).toDomain(firstEntity);
        verify(mapper).toDomain(secondEntity);
    }
}
