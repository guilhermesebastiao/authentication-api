package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RecoverPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.RecoverPassword;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaRecoverPasswordMapper {
    RecoverPasswordEntity toEntity(RecoverPassword domain);

    RecoverPassword toDomain(RecoverPasswordEntity entity);
}
