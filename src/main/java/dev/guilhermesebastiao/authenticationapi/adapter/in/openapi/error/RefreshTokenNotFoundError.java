package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RefreshTokenNotFoundError",
        description = "The refresh token could not be found."
)
public abstract class RefreshTokenNotFoundError extends BaseError<Void> {

    @Override
    @Schema(
            example = "REFRESH_TOKEN_NOT_FOUND",
            allowableValues = {"REFRESH_TOKEN_NOT_FOUND"}
    )
    public String code() {
        return "REFRESH_TOKEN_NOT_FOUND";
    }
}
