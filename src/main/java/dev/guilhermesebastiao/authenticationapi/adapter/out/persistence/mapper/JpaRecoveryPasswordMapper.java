package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoveryPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoveryPassword;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaRecoveryPasswordMapper {
    RecoveryPasswordEntity toEntity(RecoveryPassword domain);

    RecoveryPassword toDomain(RecoveryPasswordEntity entity);
}
