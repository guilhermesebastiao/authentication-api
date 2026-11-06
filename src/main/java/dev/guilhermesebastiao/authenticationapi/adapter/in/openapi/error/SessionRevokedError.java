package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SessionRevokedError",
        description = "The session has been revoked."
)
public abstract class SessionRevokedError extends BaseError<Void> {

    @Override
    @Schema(
            example = "SESSION_REVOKED",
            allowableValues = {"SESSION_REVOKED"}
    )
    public String code() {
        return "SESSION_REVOKED";
    }
}
