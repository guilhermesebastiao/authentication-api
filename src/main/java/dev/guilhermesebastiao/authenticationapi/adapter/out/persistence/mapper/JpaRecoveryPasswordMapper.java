package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoveryPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JpaRecoveryPasswordMapper {
    RecoveryPasswordEntity toEntity(RecoveryPassword domain);

    RecoveryPassword toDomain(RecoveryPasswordEntity entity);
}
