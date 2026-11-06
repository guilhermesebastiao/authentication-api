package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "InvalidGoogleTokenError",
        description = "The Google identity token is invalid."
)
public abstract class InvalidGoogleTokenError extends BaseError<Void> {

    @Override
    @Schema(
            example = "GOOGLE_TOKEN_INVALID",
            allowableValues = {"GOOGLE_TOKEN_INVALID"}
    )
    public String code() {
        return "GOOGLE_TOKEN_INVALID";
    }
}
