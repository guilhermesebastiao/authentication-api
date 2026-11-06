package dev.guilhermesebastiao.authenticationapi.adapter.in.controller;

import dev.guilhermesebastiao.authenticationapi.adapter.in.contract.ForgotPasswordApi;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ForgotPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResendForgotPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ValidateForgotPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.ForgotPasswordResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.RecoveryPasswordResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.mapper.ForgotPasswordMapper;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.ValidateForgotPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.ForgotPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.RecoveryPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController implements ForgotPasswordApi {
    private final ValidateForgotPasswordUseCase validateForgotPasswordUseCase;
    private final ResendForgotPasswordUseCase resendForgotPasswordUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ForgotPasswordMapper mapper;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiBody<ForgotPasswordResponse> forgot(
            HttpServletRequest request,
            @RequestBody @Valid ForgotPasswordRequest dto
    ) {
        ForgotPasswordOutput output = forgotPasswordUseCase.execute(dto.email());

        return ApiBody.of(mapper.toResponse(output));
    }

    @Override
    @PostMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<RecoveryPasswordResponse> validate(
            HttpServletRequest request,
            @RequestBody @Valid ValidateForgotPasswordRequest dto
    ) {
        ValidateForgotPasswordInput input = mapper.toInput(dto);

        RecoveryPasswordOutput output = validateForgotPasswordUseCase.execute(input);

        return ApiBody.of(mapper.toResponse(output));
    }

    @Override
    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<ForgotPasswordResponse> resend(
            HttpServletRequest request,
            @RequestBody @Valid ResendForgotPasswordRequest dto
    ) {
        ForgotPasswordOutput output = resendForgotPasswordUseCase.execute(dto.forgotToken());

        return ApiBody.of(mapper.toResponse(output));
    }
}
