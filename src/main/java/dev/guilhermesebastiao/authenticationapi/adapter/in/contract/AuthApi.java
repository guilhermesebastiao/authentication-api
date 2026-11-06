package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.*;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.GoogleSignInRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.RefreshTokenRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignInRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignUpRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AuthResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseSuccess;
import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.SuccessWithoutData;
import br.com.guisebastiao.authenticationapi.adapter.in.openapi.error.*;
import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error.*;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.SecurityAccount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(
        name = "Authentication",
        description = "Endpoints for authentication and account registration"
)
public interface AuthApi {

    class SuccessAuth extends BaseSuccess<AuthResponse> {}

    class SuccessActivation extends BaseSuccess<AccountActivationResponse> {}

    @Operation(
            summary = "Sign in",
            description = "Authenticates an account using its email and password."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signed in successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessAuth.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountInvalidCredentialsError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Account is not activated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountNotActivatedError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Request validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RateLimitExceededError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerError.class)
                    )
            )
    })
    ApiBody<AuthResponse> signIn(
            HttpServletRequest request,
            SignInRequest dto
    );

    @Operation(
            summary = "Sign in with Google",
            description = "Authenticates an account using a Google credential."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signed in successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessAuth.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Google token is invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = InvalidGoogleTokenError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Google authentication failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GoogleAuthenticationError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Account is not activated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountNotActivatedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Request validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ValidationError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = InternalServerError.class
                            )
                    )
            )
    })
    ApiBody<AuthResponse> googleSignIn(
            HttpServletRequest request,
            GoogleSignInRequest dto
    );

    @Operation(
            summary = "Create account",
            description = "Creates a pending account and starts its activation flow."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Account created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessActivation.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Default role not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RoleNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountAlreadyExistsError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Request validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ValidationError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = InternalServerError.class
                            )
                    )
            )
    })
    ApiBody<AccountActivationResponse> signUp(SignUpRequest dto);

    @Operation(
            summary = "Sign out",
            description = "Revokes the authenticated account's current session."
    )
    @SecurityRequirements({
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "sessionAuth")
    })
    @Parameters({
            @Parameter(
                    name = "Authorization",
                    description = "Bearer JWT access token",
                    required = true,
                    in = ParameterIn.HEADER
            ),
            @Parameter(
                    name = "X-Session",
                    description = "Authenticated session token",
                    required = true,
                    in = ParameterIn.HEADER
            )
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signed out successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessWithoutData.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UnauthorizedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "JWT validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(oneOf = {
                                    JwtExpiredError.class,
                                    JwtInvalidSignatureError.class,
                                    JwtInvalidAlgorithmError.class,
                                    JwtInvalidIssuerError.class,
                                    JwtInvalidError.class
                            })
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SessionNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Session is expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SessionExpiredError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Session is revoked",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SessionRevokedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = InternalServerError.class
                            )
                    )
            )
    })
    ApiBody<Void> signOut(
            HttpServletRequest request,
            SecurityAccount securityAccount
    );

    @Operation(
            summary = "Refresh authentication tokens",
            description = "Refreshes the authentication tokens using a valid refresh token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tokens refreshed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessAuth.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication data is unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UnauthorizedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session or refresh token not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(oneOf = {
                                    SessionNotFoundError.class,
                                    RefreshTokenNotFoundError.class
                            })
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Session or refresh token is invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(oneOf = {
                                    RefreshTokenInvalidError.class,
                                    RefreshTokenRevokedError.class,
                                    SessionExpiredError.class,
                                    SessionRevokedError.class
                            })
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Request validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ValidationError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = InternalServerError.class
                            )
                    )
            )
    })
    ApiBody<AuthResponse> refresh(
            HttpServletRequest request,
            RefreshTokenRequest dto
    );
}
