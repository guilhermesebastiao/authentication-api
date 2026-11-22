package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.ForgotPasswordEntity;
import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JpaForgotPasswordMapper {
    ForgotPasswordEntity toEntity(ForgotPassword domain);

    ForgotPassword toDomain(ForgotPasswordEntity entity);
}
