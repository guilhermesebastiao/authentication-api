package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.SessionEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JpaSessionMapper {
    SessionEntity toEntity(Session domain);

    Session toDomain(SessionEntity entity);
}
