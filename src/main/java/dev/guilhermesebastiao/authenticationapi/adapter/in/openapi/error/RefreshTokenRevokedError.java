package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RefreshTokenRevokedError",
        description = "The refresh token has been revoked."
)
public abstract class RefreshTokenRevokedError extends BaseError<Void> {

    @Override
    @Schema(
            example = "REFRESH_TOKEN_REVOKED",
            allowableValues = {"REFRESH_TOKEN_REVOKED"}
    )
    public String code() {
        return "REFRESH_TOKEN_REVOKED";
    }
}
