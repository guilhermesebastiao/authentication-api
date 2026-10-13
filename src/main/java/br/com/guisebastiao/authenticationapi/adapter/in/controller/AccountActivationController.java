package br.com.guisebastiao.authenticationapi.adapter.in.controller;

import br.com.guisebastiao.authenticationapi.adapter.in.contract.AccountActivationApi;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.AccountActivateRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.ResendAccountActivationEmailRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import br.com.guisebastiao.authenticationapi.adapter.in.mapper.AccountActivationMapper;
import br.com.guisebastiao.authenticationapi.application.command.AccountActivationCommand;
import br.com.guisebastiao.authenticationapi.application.port.in.AccountActivateUseCase;
import br.com.guisebastiao.authenticationapi.application.port.in.GetAccountActivationUseCase;
import br.com.guisebastiao.authenticationapi.application.port.in.ResendAccountActivationEmailUseCase;
import br.com.guisebastiao.authenticationapi.application.result.AccountActivationResult;
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
