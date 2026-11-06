package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SessionNotFoundError",
        description = "The session could not be found."
)
public abstract class SessionNotFoundError extends BaseError<Void> {

    @Override
    @Schema(
            example = "SESSION_NOT_FOUND",
            allowableValues = {"SESSION_NOT_FOUND"}
    )
    public String code() {
        return "SESSION_NOT_FOUND";
    }
}
