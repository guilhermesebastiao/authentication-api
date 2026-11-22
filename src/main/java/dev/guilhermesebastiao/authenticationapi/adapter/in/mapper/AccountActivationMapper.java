package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.AccountActivateRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.AccountActivationInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountActivationMapper {
    AccountActivationInput toInput(AccountActivateRequest request);

    AccountActivationResponse toResponse(AccountActivationOutput output);
}
