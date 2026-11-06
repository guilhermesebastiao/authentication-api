package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ChangePasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.DisableAccountRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountResponse;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.ChangePasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.DisableAccountInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountOutput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(AccountOutput output);

    ChangePasswordInput toInput(ChangePasswordRequest request);

    DisableAccountInput toInput(DisableAccountRequest request);
}
