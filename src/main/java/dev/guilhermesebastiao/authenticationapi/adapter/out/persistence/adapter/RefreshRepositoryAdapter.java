package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RefreshEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers.JpaRefreshMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaRefreshRepository;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshRepositoryAdapter implements RefreshRepositoryPort {
    private final JpaRefreshRepository repository;
    private final JpaRefreshMapper mapper;

    @Override
    public Refresh save(Refresh refresh) {
        RefreshEntity entity = mapper.toEntity(refresh);
        RefreshEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void save(List<Refresh> refreshes) {
        List<RefreshEntity> entities = refreshes.stream()
                .map(mapper::toEntity)
                .toList();

        repository.saveAll(entities);
    }

    @Override
    public Optional<Refresh> findByRefreshTokenHash(String refreshTokenHash) {
        return repository.findByRefreshTokenHash(refreshTokenHash).map(mapper::toDomain);
    }

    @Override
    public List<Refresh> findAllBySessionIdsAndNotRevoked(List<UUID> sessionIds) {
        return repository.findAllBySessionIdsAndNotRevoked(sessionIds).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
