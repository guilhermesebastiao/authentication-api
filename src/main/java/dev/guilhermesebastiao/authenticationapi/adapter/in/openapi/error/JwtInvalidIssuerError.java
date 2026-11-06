package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "JwtInvalidIssuerError",
        description = "The JWT issuer is invalid."
)
public abstract class JwtInvalidIssuerError extends BaseError<Void> {

    @Override
    @Schema(
            example = "JWT_INVALID_ISSUER",
            allowableValues = {"JWT_INVALID_ISSUER"}
    )
    public String code() {
        return "JWT_INVALID_ISSUER";
    }
}
