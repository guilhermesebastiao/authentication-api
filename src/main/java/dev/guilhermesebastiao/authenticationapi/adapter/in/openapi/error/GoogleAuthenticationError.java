package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "GoogleAuthenticationError",
        description = "Authentication with Google failed."
)
public abstract class GoogleAuthenticationError extends BaseError<Void> {

    @Override
    @Schema(
            example = "GOOGLE_AUTHENTICATION_FAILED",
            allowableValues = {"GOOGLE_AUTHENTICATION_FAILED"}
    )
    public String code() {
        return "GOOGLE_AUTHENTICATION_FAILED";
    }
}
