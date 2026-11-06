package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.CreateRecoverPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResetPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ValidateRecoverPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.RecoverPasswordResponse;
import dev.guilhermesebastiao.authenticationapi.application.command.CreateRecoverPasswordCommand;
import dev.guilhermesebastiao.authenticationapi.application.command.ResetPasswordCommand;
import dev.guilhermesebastiao.authenticationapi.application.command.ValidateRecoverPasswordCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.RecoverPasswordResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecoverPasswordMapper {
    CreateRecoverPasswordCommand toCommand(CreateRecoverPasswordRequest request);

    ResetPasswordCommand toCommand(ResetPasswordRequest request);

    ValidateRecoverPasswordCommand toCommand(ValidateRecoverPasswordRequest request);

    RecoverPasswordResponse toResponse(RecoverPasswordResult result);
}
