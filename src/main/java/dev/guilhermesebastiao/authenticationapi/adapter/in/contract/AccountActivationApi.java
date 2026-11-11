package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.AccountActivateRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResendAccountActivationRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountActivationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(
        name = "Account Activation",
        description = "Endpoints for account activation and activation email resend"
)
public interface AccountActivationApi {

    @Operation(
            summary = "Activate account",
            description = "Activates a pending account using a valid activation token."
    )
    void activate(
            HttpServletRequest request,
            AccountActivateRequest dto
    );

    @Operation(
            summary = "Get activate account",
            description = "Get activate account using activation token."
    )
    AccountActivationResponse getAccountActivation(
            HttpServletRequest request,
            String activationToken
    );

    @Operation(
            summary = "Resend activation email",
            description = "Resends the account activation email to the user."
    )
    AccountActivationResponse resend(
            HttpServletRequest request,
            ResendAccountActivationRequest dto
    );
}
