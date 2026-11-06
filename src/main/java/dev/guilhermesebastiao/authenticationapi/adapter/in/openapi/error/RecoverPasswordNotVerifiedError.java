package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RecoverPasswordNotVerifiedError",
        description = "The password recovery not verified."
)
public abstract class RecoverPasswordNotVerifiedError extends BaseError<Void> {

    @Override
    @Schema(
            example = "RECOVER_PASSWORD_NOT_VERIFIED",
            allowableValues = {"RECOVER_PASSWORD_NOT_VERIFIED"}
    )
    public String code() {
        return "RECOVER_PASSWORD_NOT_VERIFIED";
    }
}
