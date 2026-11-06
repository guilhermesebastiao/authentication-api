package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "JwtExpiredError",
        description = "The JWT has expired."
)
public abstract class JwtExpiredError extends BaseError<Void> {

    @Override
    @Schema(
            example = "JWT_EXPIRED",
            allowableValues = {"JWT_EXPIRED"}
    )
    public String code() {
        return "JWT_EXPIRED";
    }
}
