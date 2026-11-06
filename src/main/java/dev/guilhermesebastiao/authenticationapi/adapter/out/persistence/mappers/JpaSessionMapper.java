package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.SessionEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaSessionMapper {
    SessionEntity toEntity(Session domain);

    Session toDomain(SessionEntity entity);
}
