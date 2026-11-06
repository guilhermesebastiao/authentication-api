package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "InternalServerError",
        description = "An unexpected internal server error occurred."
)
public abstract class InternalServerError extends BaseError<Void> {

    @Override
    @Schema(
            description = "Error code",
            example = "INTERNAL_SERVER_ERROR",
            allowableValues = {"INTERNAL_SERVER_ERROR"}
    )
    public String code() {
        return "INTERNAL_SERVER_ERROR";
    }
}
