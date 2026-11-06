package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RecoverPasswordAlreadyVerifiedError",
        description = "The password recovery has already been verified."
)
public abstract class RecoverPasswordAlreadyVerifiedError extends BaseError<Void> {

    @Override
    @Schema(
            example = "RECOVER_PASSWORD_ALREADY_VERIFIED",
            allowableValues = {"RECOVER_PASSWORD_ALREADY_VERIFIED"}
    )
    public String code() {
        return "RECOVER_PASSWORD_ALREADY_VERIFIED";
    }
}
