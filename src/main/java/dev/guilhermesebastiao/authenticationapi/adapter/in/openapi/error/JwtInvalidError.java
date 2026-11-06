package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "JwtInvalidError",
        description = "The JWT is invalid."
)
public abstract class JwtInvalidError extends BaseError<Void> {

    @Override
    @Schema(
            example = "JWT_INVALID",
            allowableValues = {"JWT_INVALID"}
    )
    public String code() {
        return "JWT_INVALID";
    }
}
