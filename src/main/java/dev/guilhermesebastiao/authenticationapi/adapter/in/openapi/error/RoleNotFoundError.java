package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RoleNotFoundError",
        description = "The requested role could not be found."
)
public abstract class RoleNotFoundError extends BaseError<Void> {

    @Override
    @Schema(
            example = "ROLE_NOT_FOUND",
            allowableValues = {"ROLE_NOT_FOUND"}
    )
    public String code() {
        return "ROLE_NOT_FOUND";
    }
}
