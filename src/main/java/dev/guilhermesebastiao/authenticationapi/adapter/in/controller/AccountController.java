package dev.guilhermesebastiao.authenticationapi.adapter.in.controller;

import dev.guilhermesebastiao.authenticationapi.adapter.in.contract.AccountApi;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ChangePasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.DisableAccountRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.mapper.AccountMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.context.SecurityAccount;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.ChangePasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.DisableAccountInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ChangePasswordUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.DisableAccountUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetCurrentAccountUseCase;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountOutput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController implements AccountApi {
    private final GetCurrentAccountUseCase getCurrentAccountUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final DisableAccountUseCase disableAccountUseCase;
    private final AccountMapper mapper;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public AccountResponse me(@AuthenticationPrincipal SecurityAccount securityAccount) {
        AccountOutput output = getCurrentAccountUseCase.execute(securityAccount.account());

        return mapper.toResponse(output);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/me/password")
    public void changePassword(
            @AuthenticationPrincipal SecurityAccount securityAccount,
            @RequestBody @Valid ChangePasswordRequest dto
    ) {
        ChangePasswordInput input = mapper.toInput(dto);

        changePasswordUseCase.execute(securityAccount.account(), input);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/me")
    public void disableAccount(
            @AuthenticationPrincipal SecurityAccount securityAccount,
            @RequestBody @Valid DisableAccountRequest dto
    ) {
        DisableAccountInput input = mapper.toInput(dto);

        disableAccountUseCase.execute(securityAccount.account(), input);
    }
}
