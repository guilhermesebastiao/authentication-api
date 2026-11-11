package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ForgotPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResendForgotPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ValidateForgotPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.ForgotPasswordResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.RecoveryPasswordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(
        name = "Forgot Password",
        description = "Endpoints for password recovery"
)
public interface ForgotPasswordApi {

    @Operation(
            summary = "Create password recovery",
            description = "Starts a password recovery flow for an account."
    )
    ForgotPasswordResponse forgot(
            HttpServletRequest request,
            ForgotPasswordRequest dto
    );

    @Operation(
            summary = "Get forgot password",
            description = "Get forgot password using forgot token."
    )
    ForgotPasswordResponse getForgotPassword(
            HttpServletRequest request,
            String forgotToken
    );

    @Operation(
            summary = "Validate password recovery OTP",
            description = "Validates the OTP associated with a password recovery token."
    )
    RecoveryPasswordResponse validate(
            HttpServletRequest request,
            ValidateForgotPasswordRequest dto
    );

    @Operation(
            summary = "Resend password recovery email",
            description = "Resends the password recovery email for a valid recovery token."
    )
    ForgotPasswordResponse resend(
            HttpServletRequest request,
            ResendForgotPasswordRequest dto
    );
}
