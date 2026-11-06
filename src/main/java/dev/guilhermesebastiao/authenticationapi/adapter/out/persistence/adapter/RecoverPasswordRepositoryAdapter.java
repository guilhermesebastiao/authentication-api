package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoverPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers.JpaRecoverPasswordMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaRecoverPasswordRepository;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RecoverPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoverPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecoverPasswordRepositoryAdapter implements RecoverPasswordRepositoryPort {
    private final JpaRecoverPasswordRepository repository;
    private final JpaRecoverPasswordMapper mapper;

    @Override
    public RecoverPassword save(RecoverPassword recoverPassword) {
        RecoverPasswordEntity entity = mapper.toEntity(recoverPassword);
        RecoverPasswordEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RecoverPassword> findByRecoverTokenHash(String recoverTokenHash) {
        return repository.findByRecoverTokenHash(recoverTokenHash).map(mapper::toDomain);
    }

    @Override
    public Optional<RecoverPassword> findByRecoverTokenHashAndNotVerified(String recoverTokenHash) {
        return repository.findByRecoverTokenHashAndNotVerified(recoverTokenHash).map(mapper::toDomain);
    }

    @Override
    public int deleteAllExpired(Instant now) {
        return repository.deleteAllExpired(now);
    }
}
