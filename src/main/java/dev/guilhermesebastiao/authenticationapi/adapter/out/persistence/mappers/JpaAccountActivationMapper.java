package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.AccountActivationEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaAccountActivationMapper {
    AccountActivationEntity toEntity(AccountActivation domain);

    AccountActivation toDomain(AccountActivationEntity entity);
}
