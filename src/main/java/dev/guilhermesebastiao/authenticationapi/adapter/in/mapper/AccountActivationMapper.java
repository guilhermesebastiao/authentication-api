package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.AccountActivateRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.application.command.AccountActivationCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.AccountActivationResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountActivationMapper {
    AccountActivationCommand toCommand(AccountActivateRequest request);

    AccountActivationResponse toResponse(AccountActivationResult result);
}
