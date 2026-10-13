package br.com.guisebastiao.authenticationapi.adapter.in.controller;

import br.com.guisebastiao.authenticationapi.adapter.in.contract.RecoverPasswordApi;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.CreateRecoverPasswordRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.ResendRecoverPasswordEmailRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.ResetPasswordRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.ValidateRecoverPasswordRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.response.RecoverPasswordResponse;
import br.com.guisebastiao.authenticationapi.adapter.in.mapper.RecoverPasswordMapper;
import br.com.guisebastiao.authenticationapi.application.command.CreateRecoverPasswordCommand;
import br.com.guisebastiao.authenticationapi.application.command.ResetPasswordCommand;
import br.com.guisebastiao.authenticationapi.application.command.ValidateRecoverPasswordCommand;
import br.com.guisebastiao.authenticationapi.application.port.in.*;
import br.com.guisebastiao.authenticationapi.application.result.RecoverPasswordResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-recovery")
@RequiredArgsConstructor
public class RecoverPasswordController implements RecoverPasswordApi {
    private final ResendRecoverPasswordEmailUseCase resendRecoverPasswordEmailUseCase;
    private final ValidateRecoverPasswordUseCase validateRecoverPasswordUseCase;
    private final CreateRecoverPasswordUseCase createRecoverPasswordUseCase;
    private final GetRecoverPasswordUseCase getRecoverPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final RecoverPasswordMapper mapper;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiBody<RecoverPasswordResponse> create(
            HttpServletRequest request,
            @RequestBody @Valid CreateRecoverPasswordRequest dto
    ) {
        CreateRecoverPasswordCommand command = mapper.toCommand(dto);

        RecoverPasswordResult data = createRecoverPasswordUseCase.execute(command);

        return ApiBody.of(mapper.toResponse(data));
    }

    @Override
    @GetMapping("/{recoverToken}")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<RecoverPasswordResponse> getRecoverPassword(
            HttpServletRequest request,
            @PathVariable("recoverToken") String recoverToken
    ) {
        RecoverPasswordResult result = getRecoverPasswordUseCase.execute(recoverToken);

        return ApiBody.of(mapper.toResponse(result));
    }

    @Override
    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<RecoverPasswordResponse> validate(
            HttpServletRequest request,
            @RequestBody @Valid ValidateRecoverPasswordRequest dto
    ) {
        ValidateRecoverPasswordCommand command = mapper.toCommand(dto);

        RecoverPasswordResult data = validateRecoverPasswordUseCase.execute(command);

        return ApiBody.of(mapper.toResponse(data));
    }

    @Override
    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<RecoverPasswordResponse> resend(
            HttpServletRequest request,
            @RequestBody @Valid ResendRecoverPasswordEmailRequest dto
    ) {
        RecoverPasswordResult data = resendRecoverPasswordEmailUseCase.execute(dto.recoverToken());

        return ApiBody.of(mapper.toResponse(data));
    }

    @Override
    @PatchMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<Void> reset(
            @RequestBody @Valid ResetPasswordRequest dto
    ) {
        ResetPasswordCommand command = mapper.toCommand(dto);

        resetPasswordUseCase.execute(command);

        return ApiBody.of();
    }
}
