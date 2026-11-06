package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "JwtInvalidAlgorithmError",
        description = "The JWT algorithm is invalid."
)
public abstract class JwtInvalidAlgorithmError extends BaseError<Void> {

    @Override
    @Schema(
            example = "JWT_INVALID_ALGORITHM",
            allowableValues = {"JWT_INVALID_ALGORITHM"}
    )
    public String code() {
        return "JWT_INVALID_ALGORITHM";
    }
}
