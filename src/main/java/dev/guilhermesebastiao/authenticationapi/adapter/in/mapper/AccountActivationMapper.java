package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.AccountActivateRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.AccountActivationInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountActivationMapper {
    AccountActivationInput toInput(AccountActivateRequest request);

    AccountActivationResponse toResponse(AccountActivationOutput output);
}
