package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "UnauthorizedError",
        description = "Authentication is required to access the requested resource."
)
public abstract class UnauthorizedError extends BaseError<Void> {

    @Override
    @Schema(
            example = "UNAUTHORIZED",
            allowableValues = {"UNAUTHORIZED"}
    )
    public String code() {
        return "UNAUTHORIZED";
    }
}
