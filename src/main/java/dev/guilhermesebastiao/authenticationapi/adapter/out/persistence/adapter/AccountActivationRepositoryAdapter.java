package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.AccountActivationEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers.JpaAccountActivationMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaAccountActivationRepository;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountActivationRepositoryAdapter implements AccountActivationRepositoryPort {
    private final JpaAccountActivationRepository repository;
    private final JpaAccountActivationMapper mapper;

    @Override
    public void save(List<AccountActivation> accountActivation) {
        List<AccountActivationEntity> entities = accountActivation.stream()
                .map(mapper::toEntity)
                .toList();

        repository.saveAll(entities);
    }

    @Override
    public AccountActivation save(AccountActivation accountActivation) {
        AccountActivationEntity entity = mapper.toEntity(accountActivation);
        AccountActivationEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<AccountActivation> findByActivationTokenHash(String activationTokenHash) {
        return repository.findByActivationTokenHash(activationTokenHash).map(mapper::toDomain);
    }

    @Override
    public int deleteAllExpired(Instant now) {
        return repository.deleteAllExpired(now);
    }
}
