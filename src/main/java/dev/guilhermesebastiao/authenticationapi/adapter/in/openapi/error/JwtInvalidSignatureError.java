package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "JwtInvalidSignatureError",
        description = "The JWT signature is invalid."
)
public abstract class JwtInvalidSignatureError extends BaseError<Void> {

    @Override
    @Schema(
            example = "JWT_INVALID_SIGNATURE",
            allowableValues = {"JWT_INVALID_SIGNATURE"}
    )
    public String code() {
        return "JWT_INVALID_SIGNATURE";
    }
}
