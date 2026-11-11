package dev.guilhermesebastiao.authenticationapi.adapter.in.controller;

import dev.guilhermesebastiao.authenticationapi.adapter.in.contract.AuthApi;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.GoogleSignInRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.RefreshTokenRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignInRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignUpRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AuthResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.mapper.AuthMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.context.SecurityAccount;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.CreateAccountInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.RefreshTokenInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.input.SignInInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.*;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AuthOutput;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final RevokeSessionUseCase revokeSessionUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final GoogleSignInUseCase googleSignInUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final SignInUseCase signInUseCase;
    private final AuthMapper mapper;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-in")
    public AuthResponse signIn(
            HttpServletRequest request,
            @RequestBody @Valid SignInRequest dto
    ) {
        SignInInput input = mapper.toInput(dto);

        AuthOutput output = signInUseCase.execute(
                input,
                request.getHeader("User-Agent"),
                request.getRemoteAddr()
        );

        return mapper.toResponse(output);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/google/sign-in")
    public AuthResponse googleSignIn(
            HttpServletRequest request,
            @RequestBody @Valid GoogleSignInRequest dto
    ) {
        AuthOutput output = googleSignInUseCase.execute(
                dto.code(),
                request.getHeader("User-Agent"),
                request.getRemoteAddr()
        );

        return mapper.toResponse(output);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public AccountActivationResponse signUp(
            @RequestBody @Valid SignUpRequest dto
    ) {
        CreateAccountInput input = mapper.toInput(dto);

        AccountActivationOutput output = createAccountUseCase.execute(input);

        return mapper.toResponse(output);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/sign-out")
    public void signOut(
            HttpServletRequest request,
            @AuthenticationPrincipal SecurityAccount securityAccount
    ) {
        revokeSessionUseCase.execute(
                securityAccount.account(),
                request.getHeader("X-Session")
        );
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    public AuthResponse refresh(
            HttpServletRequest request,
            @RequestBody @Valid RefreshTokenRequest dto
    ) {
        RefreshTokenInput input = mapper.toInput(dto);

        AuthOutput output = refreshTokenUseCase.execute(input);

        return mapper.toResponse(output);
    }
}
