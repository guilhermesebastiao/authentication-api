package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.AccountEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JpaAccountMapper {
    AccountEntity toEntity(Account domain);

    Account toDomain(AccountEntity entity);
}
