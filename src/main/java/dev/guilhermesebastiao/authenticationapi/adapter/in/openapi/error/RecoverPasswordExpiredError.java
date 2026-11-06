package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RecoverPasswordExpiredError",
        description = "The password recovery token has expired."
)
public abstract class RecoverPasswordExpiredError extends BaseError<Void> {

    @Override
    @Schema(
            example = "RECOVER_PASSWORD_EXPIRED",
            allowableValues = {"RECOVER_PASSWORD_EXPIRED"}
    )
    public String code() {
        return "RECOVER_PASSWORD_EXPIRED";
    }
}
