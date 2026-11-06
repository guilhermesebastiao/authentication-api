package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "PasswordSameAsCurrentError",
        description = "The new password matches the current password."
)
public abstract class PasswordSameAsCurrentError extends BaseError<Void> {

    @Override
    @Schema(
            example = "SAME_PASSWORD",
            allowableValues = {"SAME_PASSWORD"}
    )
    public String code() {
        return "SAME_PASSWORD";
    }
}
