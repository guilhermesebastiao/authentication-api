package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.*;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.GoogleSignInRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.RefreshTokenRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignInRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignUpRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AuthResponse;
import br.com.guisebastiao.authenticationapi.application.command.*;
import dev.guilhermesebastiao.authenticationapi.application.command.CreateAccountCommand;
import dev.guilhermesebastiao.authenticationapi.application.command.GoogleSignInCommand;
import dev.guilhermesebastiao.authenticationapi.application.command.RefreshTokenCommand;
import dev.guilhermesebastiao.authenticationapi.application.command.SignInCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.AccountActivationResult;
import dev.guilhermesebastiao.authenticationapi.application.result.AuthResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthResponse toResponse(AuthResult result);

    AccountActivationResponse toResponse(AccountActivationResult result);

    SignInCommand toCommand(SignInRequest request);

    CreateAccountCommand toCommand(SignUpRequest request);

    GoogleSignInCommand toCommand(GoogleSignInRequest request);

    RefreshTokenCommand toCommand(RefreshTokenRequest request);
}
