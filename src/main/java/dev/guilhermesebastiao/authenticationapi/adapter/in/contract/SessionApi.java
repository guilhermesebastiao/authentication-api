package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiListBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.PaginationRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.RevokeSessionsRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.SessionResponse;
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
        name = "Sessions",
        description = "Endpoints for managing authenticated sessions"
)
@SecurityRequirements({
        @SecurityRequirement(name = "bearerAuth"),
        @SecurityRequirement(name = "sessionAuth")
})
public interface SessionApi {

    class SuccessSessions extends BaseSuccess<ApiListBody<SessionResponse>> {}

    class SuccessSession extends BaseSuccess<SessionResponse> {}

    @Operation(
            summary = "List account sessions",
            description = "Lists the active sessions belonging to the authenticated account."
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
                    description = "Sessions retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessSessions.class
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
    ApiBody<ApiListBody<SessionResponse>> findAllSessions(
            SecurityAccount securityAccount,
            String sessionToken,
            PaginationRequest pagination
    );

    @Operation(
            summary = "Get current session",
            description = "Retrieves the current authenticated session."
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
                    description = "Session retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessSession.class
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
                    responseCode = "403",
                    description = "Session does not belong to account",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SessionNotBelongToAccountError.class
                            )
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
    ApiBody<SessionResponse> findCurrentSession(
            SecurityAccount securityAccount,
            String sessionToken
    );

    @Operation(
            summary = "Revoke selected sessions",
            description = "Revokes selected sessions belonging to the authenticated account."
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
                    description = "Sessions revoked successfully",
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
    ApiBody<Void> revokeSession(
            SecurityAccount securityAccount,
            RevokeSessionsRequest dto
    );

    @Operation(
            summary = "Revoke all sessions",
            description = "Revokes all active sessions belonging to the authenticated account."
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
                    description = "Sessions revoked successfully",
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
    ApiBody<Void> revokeAllSessions(
            SecurityAccount securityAccount
    );
}
