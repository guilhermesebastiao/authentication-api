package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.SessionEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers.JpaSessionMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaSessionRepository;
import dev.guilhermesebastiao.authenticationapi.application.result.PageResult;
import dev.guilhermesebastiao.authenticationapi.application.command.PageQueryCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionRepositoryAdapter implements SessionRepositoryPort {
    private final JpaSessionRepository repository;
    private final JpaSessionMapper mapper;

    @Override
    public Session save(Session session) {
        SessionEntity entity = mapper.toEntity(session);
        SessionEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void save(List<Session> sessions) {
        List<SessionEntity> entities = sessions.stream()
                .map(mapper::toEntity)
                .toList();

        repository.saveAll(entities);
    }

    @Override
    public Optional<Session> findBySessionTokenHash(String sessionTokenHash) {
        return repository.findBySessionTokenHash(sessionTokenHash).map(mapper::toDomain);
    }

    @Override
    public PageResult<Session> findAllByAccountIdAndNotRevoked(UUID accountId, PageQueryCommand pagination) {
        Pageable pageable = PageRequest.of(
                pagination.zeroBasedPage(),
                pagination.size(),
                Sort.by(Sort.Direction.DESC, "lastSeenAt")
        );

        Page<SessionEntity> result = repository.findAllByAccountIdAndNotRevoked(accountId, pageable);

        List<Session> sessions = result.getContent()
                .stream()
                .map(mapper::toDomain)
                .toList();

        return new PageResult<>(
                sessions,
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
    }

    @Override
    public List<Session> findAllByAccountIdAndNotRevoked(UUID accountId) {
        return repository.findAllByAccountIdAndNotRevoked(accountId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Session> findByAccountIdAndSessionTokenHash(UUID accountId, String sessionTokenHash) {
        return repository.findByAccountIdAndSessionTokenHash(accountId, sessionTokenHash).map(mapper::toDomain);
    }

    @Override
    public List<Session> findAllByAccountIdAndSessionIdsAndNotRevoked(UUID accountId, List<UUID> sessionIds) {
        return repository.findAllByAccountIdAndSessionIdsAndNotRevoked(accountId, sessionIds)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public int deleteAllRevoked(Instant now) {
        return repository.deleteAllRevokedOrExpired(now);
    }
}
