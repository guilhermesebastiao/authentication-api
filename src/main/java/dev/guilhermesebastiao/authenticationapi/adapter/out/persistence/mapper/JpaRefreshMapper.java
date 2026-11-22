package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RefreshEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JpaRefreshMapper {
    RefreshEntity toEntity(Refresh domain);

    Refresh toDomain(RefreshEntity entity);
}
