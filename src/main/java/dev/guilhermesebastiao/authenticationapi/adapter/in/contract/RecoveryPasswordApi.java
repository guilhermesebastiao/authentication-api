package dev.guilhermesebastiao.authenticationapi.adapter.in.contract;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResetPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.RecoveryPasswordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(
        name = "Password Recovery",
        description = "Endpoints for password recovery"
)
public interface RecoveryPasswordApi {

    @Operation(
            summary = "Get recovery password",
            description = "Get recovery password using recovery token."
    )
    RecoveryPasswordResponse getRecoverPassword(
            HttpServletRequest request,
            String recoverToken
    );

    @Operation(
            summary = "Reset password",
            description = "Changes the account password using a verified recovery token."
    )
    void reset(ResetPasswordRequest dto);
}
