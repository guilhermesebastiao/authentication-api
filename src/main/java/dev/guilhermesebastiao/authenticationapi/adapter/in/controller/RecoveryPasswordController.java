package dev.guilhermesebastiao.authenticationapi.adapter.in.controller;

import dev.guilhermesebastiao.authenticationapi.adapter.in.contract.RecoveryPasswordApi;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResetPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.RecoveryPasswordResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.mapper.RecoveryPasswordMapper;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.ResetPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.RecoveryPasswordOutput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recovery-password")
@RequiredArgsConstructor
public class RecoveryPasswordController implements RecoveryPasswordApi {
    private final GetRecoveryPasswordUseCase getRecoveryPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final RecoveryPasswordMapper mapper;


    @Override
    @GetMapping("/{recoveryToken}")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<RecoveryPasswordResponse> getRecoverPassword(
            HttpServletRequest request,
            @PathVariable("recoverToken") String recoveryToken
    ) {
        RecoveryPasswordOutput output = getRecoveryPasswordUseCase.execute(recoveryToken);

        return ApiBody.of(mapper.toResponse(output));
    }

    @Override
    @PatchMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<Void> reset(
            @RequestBody @Valid ResetPasswordRequest dto
    ) {
        ResetPasswordInput input = mapper.toInput(dto);

        resetPasswordUseCase.execute(input);

        return ApiBody.of();
    }
}
