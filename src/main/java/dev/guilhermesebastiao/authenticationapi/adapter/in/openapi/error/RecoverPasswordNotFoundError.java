package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RecoverPasswordNotFoundError",
        description = "The password recovery request could not be found."
)
public abstract class RecoverPasswordNotFoundError extends BaseError<Void> {

    @Override
    @Schema(
            example = "RECOVER_PASSWORD_NOT_FOUND",
            allowableValues = {"RECOVER_PASSWORD_NOT_FOUND"}
    )
    public String code() {
        return "RECOVER_PASSWORD_NOT_FOUND";
    }
}
