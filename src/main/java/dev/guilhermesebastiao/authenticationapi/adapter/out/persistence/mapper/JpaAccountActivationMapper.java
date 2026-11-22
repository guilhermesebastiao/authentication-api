package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.AccountActivationEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JpaAccountActivationMapper {
    AccountActivationEntity toEntity(AccountActivation domain);

    AccountActivation toDomain(AccountActivationEntity entity);
}
