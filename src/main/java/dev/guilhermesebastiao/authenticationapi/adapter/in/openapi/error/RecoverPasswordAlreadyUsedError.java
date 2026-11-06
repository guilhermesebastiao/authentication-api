package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RecoverPasswordAlreadyUsedError",
        description = "The password recovery token has already been used."
)
public abstract class RecoverPasswordAlreadyUsedError extends BaseError<Void> {

    @Override
    @Schema(
            example = "RECOVER_PASSWORD_ALREADY_USED",
            allowableValues = {"RECOVER_PASSWORD_ALREADY_USED"}
    )
    public String code() {
        return "RECOVER_PASSWORD_ALREADY_USED";
    }
}
