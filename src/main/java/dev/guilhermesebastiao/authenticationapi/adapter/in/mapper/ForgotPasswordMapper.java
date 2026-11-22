package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ValidateForgotPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.ForgotPasswordResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.RecoveryPasswordResponse;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.ValidateForgotPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.ForgotPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.RecoveryPasswordOutput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ForgotPasswordMapper {
    ValidateForgotPasswordInput toInput(ValidateForgotPasswordRequest request);

    ForgotPasswordResponse toResponse(ForgotPasswordOutput output);

    RecoveryPasswordResponse toResponse(RecoveryPasswordOutput output);
}
