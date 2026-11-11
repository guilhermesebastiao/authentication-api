package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.ForgotPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper.JpaForgotPasswordMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaForgotPasswordRepository;
import dev.guilhermesebastiao.authenticationapi.application.port.out.ForgotPasswordRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ForgotPasswordRepositoryAdapter implements ForgotPasswordRepositoryPort {
    private final JpaForgotPasswordRepository repository;
    private final JpaForgotPasswordMapper mapper;

    @Override
    public ForgotPassword save(ForgotPassword forgotPassword) {
        ForgotPasswordEntity entity = mapper.toEntity(forgotPassword);
        ForgotPasswordEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ForgotPassword> findByForgotTokenHash(String forgotTokenHash) {
        return repository.findByForgotTokenHash(forgotTokenHash).map(mapper::toDomain);
    }

    @Override
    public long deleteAllExpiredOrUsed(Instant now) {
        return repository.deleteAllExpiredOrUsed(now);
    }
}
