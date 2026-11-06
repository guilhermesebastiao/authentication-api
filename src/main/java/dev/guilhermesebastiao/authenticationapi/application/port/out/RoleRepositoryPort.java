package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.domain.model.Role;

import java.util.Optional;

public interface RoleRepositoryPort {
    Optional<Role> findRoleByName(String name);
}
