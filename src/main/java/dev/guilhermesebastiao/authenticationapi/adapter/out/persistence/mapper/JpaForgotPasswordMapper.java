package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.ForgotPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaForgotPasswordMapper {
    ForgotPasswordEntity toEntity(ForgotPassword domain);

    ForgotPassword toDomain(ForgotPasswordEntity entity);
}
