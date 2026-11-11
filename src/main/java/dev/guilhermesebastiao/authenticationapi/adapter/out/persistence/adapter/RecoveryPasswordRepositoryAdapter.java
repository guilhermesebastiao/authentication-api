package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoveryPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper.JpaRecoveryPasswordMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaRecoveryPasswordRepository;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoveryPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecoveryPasswordRepositoryAdapter implements RecoveryPasswordRepositoryPort {
    private final JpaRecoveryPasswordRepository repository;
    private final JpaRecoveryPasswordMapper mapper;

    @Override
    public RecoveryPassword save(RecoveryPassword recoveryPassword) {
        RecoveryPasswordEntity entity = mapper.toEntity(recoveryPassword);
        RecoveryPasswordEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RecoveryPassword> findByRecoveryTokenHash(String recoveryTokenHash) {
        return repository.findByRecoveryTokenHash(recoveryTokenHash).map(mapper::toDomain);
    }

    @Override
    public long deleteAllExpiredOrUsed(Instant now) {
        return repository.deleteAllExpiredOrUsed(now);
    }
}
