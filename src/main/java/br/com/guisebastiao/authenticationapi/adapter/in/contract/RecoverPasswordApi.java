package br.com.guisebastiao.authenticationapi.adapter.in.contract;

import br.com.guisebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.CreateRecoverPasswordRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.ResendRecoverPasswordEmailRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.ResetPasswordRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.ValidateRecoverPasswordRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.response.RecoverPasswordResponse;
import br.com.guisebastiao.authenticationapi.adapter.in.openapi.common.BaseSuccess;
import br.com.guisebastiao.authenticationapi.adapter.in.openapi.common.SuccessWithoutData;
import br.com.guisebastiao.authenticationapi.adapter.in.openapi.error.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(
        name = "Password Recovery",
        description = "Endpoints for password recovery"
)
public interface RecoverPasswordApi {

    class SuccessRecovery extends BaseSuccess<RecoverPasswordResponse> {}

    @Operation(
            summary = "Create password recovery",
            description = "Starts a password recovery flow for an account."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Password recovery created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessRecovery.class
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
                    responseCode = "429",
                    description = "Too many requests",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RateLimitExceededError.class
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
    ApiBody<RecoverPasswordResponse> create(
            HttpServletRequest request,
            CreateRecoverPasswordRequest dto
    );

    @Operation(
            summary = "Get recover password",
            description = "Get recover password using recover token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Account recover returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessRecovery.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recover password not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Recover password is already used",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordAlreadyUsedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "410",
                    description = "Recover password token has expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordExpiredError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests",
                    content = @Content(
                            schema = @Schema(
                                    implementation = RateLimitExceededError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = InternalServerError.class
                            )
                    )
            )
    })
    ApiBody<RecoverPasswordResponse> getRecoverPassword(
            HttpServletRequest request,
            String recoverToken
    );

    @Operation(
            summary = "Validate password recovery OTP",
            description = "Validates the OTP associated with a password recovery token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OTP validated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessRecovery.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "OTP is invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = IncorrectOtpError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recovery token not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Recovery token was already used",
                    content = @Content(
                            mediaType = "application/json", schema = @Schema(implementation = RecoverPasswordAlreadyUsedError.class))
            ),
            @ApiResponse(
                    responseCode = "410",
                    description = "Recovery token expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordExpiredError.class
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
    ApiBody<RecoverPasswordResponse> validate(
            HttpServletRequest request,
            ValidateRecoverPasswordRequest dto
    );

    @Operation(
            summary = "Resend password recovery email",
            description = "Resends the password recovery email for a valid recovery token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Email sent successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessRecovery.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recovery token not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Recovery token already verified",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordAlreadyVerifiedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "410",
                    description = "Recovery token expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordExpiredError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Recovery email resend is not available yet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordResendNotAvailableError.class
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
    ApiBody<RecoverPasswordResponse> resend(
            HttpServletRequest request,
            ResendRecoverPasswordEmailRequest dto
    );

    @Operation(
            summary = "Reset password",
            description = "Changes the account password using a verified recovery token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessWithoutData.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recovery token not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Recovery token already used",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordAlreadyUsedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Recovery token has not been verified",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordNotVerifiedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "410",
                    description = "Recovery token expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecoverPasswordExpiredError.class
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
    ApiBody<Void> reset(
            ResetPasswordRequest dto
    );
}
