package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mappers;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.AccountEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaAccountMapper {
    AccountEntity toEntity(Account domain);

    Account toDomain(AccountEntity entity);
}
