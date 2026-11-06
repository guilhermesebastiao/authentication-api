package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper.JpaRoleMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaRoleRepository;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RoleRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {
    private final JpaRoleRepository repository;
    private final JpaRoleMapper mapper;

    @Override
    public Optional<Role> findRoleByName(String name) {
        return repository.findByName(name).map(mapper::toDomain);
    }
}
