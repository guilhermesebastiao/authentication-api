package br.com.guisebastiao.authenticationapi.adapter.in.contract;

import br.com.guisebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.AccountActivateRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.request.ResendAccountActivationEmailRequest;
import br.com.guisebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
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
        name = "Account Activation",
        description = "Endpoints for account activation and activation email resend"
)
public interface AccountActivationApi {

    class SuccessResend extends BaseSuccess<AccountActivationResponse> {}

    class SuccessAccountActivation extends BaseSuccess<AccountActivationResponse> {}

    @Operation(
            summary = "Activate account",
            description = "Activates a pending account using a valid activation token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Account activated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessWithoutData.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Request validation OTP failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = IncorrectOtpError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account activation not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountActivationNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account is already activated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountAlreadyActivatedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "410",
                    description = "Account activation token has expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountActivationExpiredError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "The request contains one or more validation errors.",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ValidationError.class
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
    ApiBody<Void> activate(
            HttpServletRequest request,
            AccountActivateRequest dto
    );

    @Operation(
            summary = "Get activate account",
            description = "Get activate account using activation token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Account activation returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessAccountActivation.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account activation not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountActivationNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account is already activated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountAlreadyActivatedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "410",
                    description = "Account activation token has expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountActivationExpiredError.class
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
    ApiBody<AccountActivationResponse> getAccountActivation(
            HttpServletRequest request,
            String activationToken
    );

    @Operation(
            summary = "Resend activation email",
            description = "Resends the account activation email to the user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Email sent successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessResend.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account activation not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountActivationNotFoundError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account is already activated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountAlreadyActivatedError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "410",
                    description = "Account activation token has expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountActivationExpiredError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "The request contains one or more validation errors.",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ValidationError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Account activation email resend is not available yet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AccountActivationResendNotAvailableError.class
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
    ApiBody<AccountActivationResponse> resend(
            HttpServletRequest request,
            ResendAccountActivationEmailRequest dto
    );
}
