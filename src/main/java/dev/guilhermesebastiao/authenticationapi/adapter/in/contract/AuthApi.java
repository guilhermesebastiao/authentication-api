package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.GoogleSignInRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.RefreshTokenRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignInRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignUpRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AuthResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.context.SecurityAccount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(
        name = "Authentication",
        description = "Endpoints for authentication and account registration"
)
public interface AuthApi {

    @Operation(
            summary = "Sign in",
            description = "Authenticates an account using its email and password."
    )
    AuthResponse signIn(
            HttpServletRequest request,
            SignInRequest dto
    );

    @Operation(
            summary = "Sign in with Google",
            description = "Authenticates an account using a Google credential."
    )
    AuthResponse googleSignIn(
            HttpServletRequest request,
            GoogleSignInRequest dto
    );

    @Operation(
            summary = "Create account",
            description = "Creates a pending account and starts its activation flow."
    )
    AccountActivationResponse signUp(SignUpRequest dto);

    @Operation(
            summary = "Sign out",
            description = "Revokes the authenticated account's current session."
    )
    @SecurityRequirements({
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "sessionAuth")
    })
    void signOut(
            HttpServletRequest request,
            SecurityAccount securityAccount
    );

    @Operation(
            summary = "Refresh authentication tokens",
            description = "Refreshes the authentication tokens using a valid refresh token."
    )
    AuthResponse refresh(
            HttpServletRequest request,
            RefreshTokenRequest dto
    );
}
