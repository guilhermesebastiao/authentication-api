package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RefreshTokenInvalidError",
        description = "The refresh token is invalid."
)
public abstract class RefreshTokenInvalidError extends BaseError<Void> {

    @Override
    @Schema(
            example = "REFRESH_TOKEN_INVALID",
            allowableValues = {"REFRESH_TOKEN_INVALID"}
    )
    public String code() {
        return "REFRESH_TOKEN_INVALID";
    }
}
