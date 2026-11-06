package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "JwtValidationError",
        description = "The JWT validation failed."
)
public abstract class JwtValidationError extends BaseError<Void> {

    @Override
    @Schema(
            example = "JWT_INVALID",
            allowableValues = {
                    "JWT_EXPIRED",
                    "JWT_INVALID_SIGNATURE",
                    "JWT_INVALID_ALGORITHM",
                    "JWT_INVALID_ISSUER",
                    "JWT_INVALID"
            }
    )
    public abstract String code();
}
