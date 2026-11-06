package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ChangePasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.DisableAccountRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountResponse;
import dev.guilhermesebastiao.authenticationapi.application.command.ChangePasswordCommand;
import dev.guilhermesebastiao.authenticationapi.application.command.DisableAccountCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.AccountResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(AccountResult result);

    ChangePasswordCommand toCommand(ChangePasswordRequest request);

    DisableAccountCommand toCommand(DisableAccountRequest request);
}
