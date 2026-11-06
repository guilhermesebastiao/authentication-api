package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ChangePasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.DisableAccountRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.SuccessWithoutData;
import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error.*;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.SecurityAccount;
import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseSuccess;
import br.com.guisebastiao.authenticationapi.adapter.in.openapi.error.*;
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

@Tag(
        name = "Account",
        description = "Endpoints for the authenticated account"
)
@SecurityRequirements({
        @SecurityRequirement(name = "bearerAuth"),
        @SecurityRequirement(name = "sessionAuth")
})
public interface AccountApi {

    class SuccessAccount extends BaseSuccess<AccountResponse> {}

    @Operation(
            summary = "Get current account",
            description = "Retrieves the account information of the currently authenticated user."
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
                    description = "Account retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessAccount.class
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
    ApiBody<AccountResponse> me(
            SecurityAccount securityAccount
    );

    @Operation(
            summary = "Change account password",
            description = "Changes the password of the currently authenticated account."
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
                    description = "Password changed successfully",
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
                    responseCode = "401",
                    description = "Current password is invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountInvalidCredentialsError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "New password is the current password",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = PasswordSameAsCurrentError.class
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
    ApiBody<Void> changePassword(
            SecurityAccount securityAccount,
            ChangePasswordRequest dto
    );

    @Operation(
            summary = "Disable current account",
            description = "Disables the currently authenticated account and revokes all active sessions."
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
                    description = "Account disabled successfully",
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
                    responseCode = "401",
                    description = "Password is invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountInvalidCredentialsError.class
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
    ApiBody<Void> disableAccount(
            SecurityAccount securityAccount,
            DisableAccountRequest dto
    );
}
