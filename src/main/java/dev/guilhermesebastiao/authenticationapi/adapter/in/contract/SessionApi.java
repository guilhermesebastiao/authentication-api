package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiPageBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.PaginationParams;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.SessionResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.context.SecurityAccount;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "List account sessions",
            description = "Lists the active sessions belonging to the authenticated account."
    )
    ApiPageBody<SessionResponse> findAllSessions(
            SecurityAccount securityAccount,
            String sessionToken,
            PaginationParams pagination
    );

    @Operation(
            summary = "Get current session",
            description = "Retrieves the current authenticated session."
    )
    SessionResponse findCurrentSession(
            SecurityAccount securityAccount,
            String sessionToken
    );

    @Operation(
            summary = "Revoke session",
            description = "Revoke session using id and belonging to the authenticated account."
    )
    void revokeSession(
            SecurityAccount securityAccount,
            String sessionId
    );

    @Operation(
            summary = "Revoke all sessions",
            description = "Revokes all active sessions belonging to the authenticated account."
    )
    void revokeAllSessions(SecurityAccount securityAccount);
}
