package dev.guilhermesebastiao.authenticationapi.adapter.in.controller;

import dev.guilhermesebastiao.authenticationapi.adapter.in.contract.AccountActivationApi;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.AccountActivateRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResendAccountActivationRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.mapper.AccountActivationMapper;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.AccountActivationInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.AccountActivateUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ResendAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account-activation")
@RequiredArgsConstructor
public class AccountActivationController implements AccountActivationApi {
    private final ResendAccountActivationUseCase resendAccountActivationUseCase;
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
        AccountActivationInput input = mapper.toInput(dto);

        accountActivateUseCase.execute(input);

        return ApiBody.of();
    }

    @Override
    @GetMapping("/{activationToken}")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<AccountActivationResponse> getAccountActivation(
            HttpServletRequest request,
            @PathVariable String activationToken
    ) {
        AccountActivationOutput output = getAccountActivationUseCase.execute(activationToken);

        return ApiBody.of(mapper.toResponse(output));
    }

    @Override
    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<AccountActivationResponse> resend(
            HttpServletRequest request,
            @RequestBody @Valid ResendAccountActivationRequest dto
    ) {
        AccountActivationOutput output = resendAccountActivationUseCase.execute(dto.activationToken());

        return ApiBody.of(mapper.toResponse(output));
    }
}
