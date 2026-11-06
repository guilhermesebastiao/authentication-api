package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SessionExpiredError",
        description = "The session has expired."
)
public abstract class SessionExpiredError extends BaseError<Void> {

    @Override
    @Schema(
            example = "SESSION_EXPIRED",
            allowableValues = {"SESSION_EXPIRED"}
    )
    public String code() {
        return "SESSION_EXPIRED";
    }
}
