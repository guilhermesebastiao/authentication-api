package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RoleEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaRoleMapper {
    RoleEntity toEntity(Role domain);

    Role toDomain(RoleEntity entity);
}
