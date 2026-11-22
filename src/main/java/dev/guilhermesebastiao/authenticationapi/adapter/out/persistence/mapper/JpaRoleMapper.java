package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RoleEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JpaRoleMapper {
    RoleEntity toEntity(Role domain);

    Role toDomain(RoleEntity entity);
}
