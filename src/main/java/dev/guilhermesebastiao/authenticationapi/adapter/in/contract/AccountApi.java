package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ChangePasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.DisableAccountRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.AccountResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.context.SecurityAccount;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Get current account",
            description = "Retrieves the account information of the currently authenticated user."
    )
    AccountResponse me(SecurityAccount securityAccount);

    @Operation(
            summary = "Change account password",
            description = "Changes the password of the currently authenticated account."
    )
    void changePassword(
            SecurityAccount securityAccount,
            ChangePasswordRequest dto
    );

    @Operation(
            summary = "Disable current account",
            description = "Disables the currently authenticated account and revokes all active sessions."
    )
    void disableAccount(
            SecurityAccount securityAccount,
            DisableAccountRequest dto
    );
}
