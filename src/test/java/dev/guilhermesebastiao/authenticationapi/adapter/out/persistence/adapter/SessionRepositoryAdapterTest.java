package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.SessionEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers.JpaSessionMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaSessionRepository;
import dev.guilhermesebastiao.authenticationapi.application.command.PageQueryCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.PageResult;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessionRepositoryAdapterTest {

    @Mock
    private JpaSessionRepository repository;

    @Mock
    private JpaSessionMapper mapper;

    @InjectMocks
    private SessionRepositoryAdapter adapter;

    @Test
    @DisplayName("Should return the mapped session after saving the session entity")
    void givenSessionAndMappedEntities_whenSave_thenReturnSavedSession() {
        Session session = new Session();
        SessionEntity entity = new SessionEntity();
        SessionEntity savedEntity = new SessionEntity();
        Session savedSession = new Session();

        given(mapper.toEntity(session))
                .willReturn(entity);

        given(repository.save(entity))
                .willReturn(savedEntity);

        given(mapper.toDomain(savedEntity))
                .willReturn(savedSession);

        Session result = adapter.save(session);

        assertSame(savedSession, result);
        verify(mapper).toEntity(session);
        verify(repository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Should map all sessions before saving them")
    void givenSessions_whenSaveAll_thenMapAndSaveAllEntities() {
        Session firstSession = new Session();
        Session secondSession = new Session();
        SessionEntity firstEntity = new SessionEntity();
        SessionEntity secondEntity = new SessionEntity();

        given(mapper.toEntity(firstSession))
                .willReturn(firstEntity);

        given(mapper.toEntity(secondSession))
                .willReturn(secondEntity);

        adapter.save(List.of(firstSession, secondSession));

        verify(mapper).toEntity(firstSession);
        verify(mapper).toEntity(secondSession);
        verify(repository).saveAll(List.of(firstEntity, secondEntity));
    }

    @Test
    @DisplayName("Should return the mapped session when the session token hash exists")
    void givenExistingEntity_whenFindBySessionTokenHash_thenReturnMappedSession() {
        String sessionTokenHash = "session-token-hash";
        SessionEntity entity = new SessionEntity();
        Session session = new Session();

        given(repository.findBySessionTokenHash(sessionTokenHash))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(session);

        Optional<Session> result = adapter.findBySessionTokenHash(sessionTokenHash);

        assertTrue(result.isPresent());
        assertSame(session, result.get());
        verify(repository).findBySessionTokenHash(sessionTokenHash);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when the session token hash does not exist")
    void givenMissingEntity_whenFindBySessionTokenHash_thenReturnEmptyOptional() {
        String sessionTokenHash = "missing-session-token-hash";

        given(repository.findBySessionTokenHash(sessionTokenHash))
                .willReturn(Optional.empty());

        Optional<Session> result = adapter.findBySessionTokenHash(sessionTokenHash);

        assertTrue(result.isEmpty());
        verify(repository).findBySessionTokenHash(sessionTokenHash);
        verify(mapper, never()).toDomain(any(SessionEntity.class));
    }

    @Test
    @DisplayName("Should convert the page query and map the paginated sessions")
    void givenPagedSessionEntities_whenFindAllByAccountIdAndNotRevoked_thenReturnMappedPage() {
        UUID accountId = UUID.randomUUID();
        PageQueryCommand pagination = new PageQueryCommand(2, 2);
        SessionEntity firstEntity = new SessionEntity();
        SessionEntity secondEntity = new SessionEntity();
        Session firstSession = new Session();
        Session secondSession = new Session();
        Pageable resultPageable = PageRequest.of(1, 2);

        Page<SessionEntity> page = new PageImpl<>(
                List.of(firstEntity, secondEntity),
                resultPageable,
                5
        );

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        given(repository.findAllByAccountIdAndNotRevoked(eq(accountId), any(Pageable.class)))
                .willReturn(page);

        given(mapper.toDomain(firstEntity))
                .willReturn(firstSession);

        given(mapper.toDomain(secondEntity))
                .willReturn(secondSession);

        PageResult<Session> result = adapter.findAllByAccountIdAndNotRevoked(accountId, pagination);

        verify(repository).findAllByAccountIdAndNotRevoked(eq(accountId), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertEquals(1, pageable.getPageNumber());
        assertEquals(2, pageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.DESC, "lastSeenAt"), pageable.getSort());
        assertEquals(List.of(firstSession, secondSession), result.content());
        assertEquals(5L, result.totalItems());
        assertEquals(3, result.totalPages());
        assertEquals(1, result.page());
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should return mapped sessions for an account")
    void givenSessionEntities_whenFindAllByAccountIdAndNotRevoked_thenReturnMappedSessions() {
        UUID accountId = UUID.randomUUID();
        SessionEntity firstEntity = new SessionEntity();
        SessionEntity secondEntity = new SessionEntity();
        Session firstSession = new Session();
        Session secondSession = new Session();

        given(repository.findAllByAccountIdAndNotRevoked(accountId))
                .willReturn(List.of(firstEntity, secondEntity));

        given(mapper.toDomain(firstEntity))
                .willReturn(firstSession);

        given(mapper.toDomain(secondEntity))
                .willReturn(secondSession);

        List<Session> result = adapter.findAllByAccountIdAndNotRevoked(accountId);

        assertEquals(List.of(firstSession, secondSession), result);
        verify(repository).findAllByAccountIdAndNotRevoked(accountId);
        verify(mapper).toDomain(firstEntity);
        verify(mapper).toDomain(secondEntity);
    }

    @Test
    @DisplayName("Should return the mapped session when the account and session token hash match")
    void givenExistingEntity_whenFindByAccountIdAndSessionTokenHash_thenReturnMappedSession() {
        UUID accountId = UUID.randomUUID();
        String sessionTokenHash = "session-token-hash";
        SessionEntity entity = new SessionEntity();
        Session session = new Session();

        given(repository.findByAccountIdAndSessionTokenHash(accountId, sessionTokenHash))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(session);

        Optional<Session> result = adapter.findByAccountIdAndSessionTokenHash(accountId, sessionTokenHash);

        assertTrue(result.isPresent());
        assertSame(session, result.get());
        verify(repository).findByAccountIdAndSessionTokenHash(accountId, sessionTokenHash);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when the account and session token hash do not match")
    void givenMissingEntity_whenFindByAccountIdAndSessionTokenHash_thenReturnEmptyOptional() {
        UUID accountId = UUID.randomUUID();
        String sessionTokenHash = "missing-session-token-hash";

        given(repository.findByAccountIdAndSessionTokenHash(accountId, sessionTokenHash))
                .willReturn(Optional.empty());

        Optional<Session> result = adapter.findByAccountIdAndSessionTokenHash(accountId, sessionTokenHash);

        assertTrue(result.isEmpty());
        verify(repository).findByAccountIdAndSessionTokenHash(accountId, sessionTokenHash);
        verify(mapper, never()).toDomain(any(SessionEntity.class));
    }

    @Test
    @DisplayName("Should return mapped sessions for an account and session ids")
    void givenSessionEntities_whenFindAllByAccountIdAndSessionIdsAndNotRevoked_thenReturnMappedSessions() {
        UUID accountId = UUID.randomUUID();
        UUID firstSessionId = UUID.randomUUID();
        UUID secondSessionId = UUID.randomUUID();
        List<UUID> sessionIds = List.of(firstSessionId, secondSessionId);
        SessionEntity firstEntity = new SessionEntity();
        SessionEntity secondEntity = new SessionEntity();
        Session firstSession = new Session();
        Session secondSession = new Session();

        given(repository.findAllByAccountIdAndSessionIdsAndNotRevoked(accountId, sessionIds))
                .willReturn(List.of(firstEntity, secondEntity));

        given(mapper.toDomain(firstEntity))
                .willReturn(firstSession);

        given(mapper.toDomain(secondEntity))
                .willReturn(secondSession);

        List<Session> result = adapter.findAllByAccountIdAndSessionIdsAndNotRevoked(accountId, sessionIds);

        assertEquals(List.of(firstSession, secondSession), result);
        verify(repository).findAllByAccountIdAndSessionIdsAndNotRevoked(accountId, sessionIds);
        verify(mapper).toDomain(firstEntity);
        verify(mapper).toDomain(secondEntity);
    }
}
