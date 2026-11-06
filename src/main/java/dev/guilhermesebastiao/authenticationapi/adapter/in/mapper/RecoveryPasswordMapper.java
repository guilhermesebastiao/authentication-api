package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResetPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.RecoveryPasswordResponse;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.ResetPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.RecoveryPasswordOutput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecoveryPasswordMapper {
    ResetPasswordInput toInput(ResetPasswordRequest request);

    RecoveryPasswordResponse toResponse(RecoveryPasswordOutput output);
}
