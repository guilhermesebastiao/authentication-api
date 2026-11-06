package dev.guilhermesebastiao.authenticationapi.adapter.in.controller;

import dev.guilhermesebastiao.authenticationapi.adapter.in.contract.AccountActivationApi;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.AccountActivateRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResendAccountActivationEmailRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.mapper.AccountActivationMapper;
import dev.guilhermesebastiao.authenticationapi.application.command.AccountActivationCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.in.AccountActivateUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ResendAccountActivationEmailUseCase;
import dev.guilhermesebastiao.authenticationapi.application.result.AccountActivationResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account-activation")
@RequiredArgsConstructor
public class AccountActivationController implements AccountActivationApi {
    private final ResendAccountActivationEmailUseCase resendAccountActivationEmailUseCase;
    private final GetAccountActivationUseCase getAccountActivationUseCase;
    private final AccountActivateUseCase accountActivateUseCase;
    private final AccountActivationMapper mapper;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<Void> activate(
            HttpServletRequest request,
            @RequestBody @Valid AccountActivateRequest dto
    ) {
        AccountActivationCommand command = mapper.toCommand(dto);

        accountActivateUseCase.execute(command);

        return ApiBody.of();
    }

    @Override
    @GetMapping("/{activationToken}")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<AccountActivationResponse> getAccountActivation(
            HttpServletRequest request,
            @PathVariable String activationToken
    ) {
        AccountActivationResult data = getAccountActivationUseCase.execute(activationToken);

        return ApiBody.of(mapper.toResponse(data));
    }

    @Override
    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<AccountActivationResponse> resend(
            HttpServletRequest request,
            @RequestBody @Valid ResendAccountActivationEmailRequest dto
    ) {
        AccountActivationResult data = resendAccountActivationEmailUseCase.execute(dto.activationToken());

        return ApiBody.of(mapper.toResponse(data));
    }
}
