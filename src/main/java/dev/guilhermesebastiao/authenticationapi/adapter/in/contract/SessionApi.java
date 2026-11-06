package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiListBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.PaginationParams;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.RevokeSessionsRequest;
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
    ApiBody<ApiListBody<SessionResponse>> findAllSessions(
            SecurityAccount securityAccount,
            String sessionToken,
            PaginationParams pagination
    );

    @Operation(
            summary = "Get current session",
            description = "Retrieves the current authenticated session."
    )
    ApiBody<SessionResponse> findCurrentSession(
            SecurityAccount securityAccount,
            String sessionToken
    );

    @Operation(
            summary = "Revoke selected sessions",
            description = "Revokes selected sessions belonging to the authenticated account."
    )
    ApiBody<Void> revokeSession(
            SecurityAccount securityAccount,
            RevokeSessionsRequest dto
    );

    @Operation(
            summary = "Revoke all sessions",
            description = "Revokes all active sessions belonging to the authenticated account."
    )
    ApiBody<Void> revokeAllSessions(
            SecurityAccount securityAccount
    );
}
