package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.RefreshTokenRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignInRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignUpRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AuthResponse;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.CreateAccountInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.RefreshTokenInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.SignInInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AuthOutput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthResponse toResponse(AuthOutput output);

    AccountActivationResponse toResponse(AccountActivationOutput output);

    SignInInput toInput(SignInRequest request);

    CreateAccountInput toInput(SignUpRequest request);

    RefreshTokenInput toInput(RefreshTokenRequest request);
}
