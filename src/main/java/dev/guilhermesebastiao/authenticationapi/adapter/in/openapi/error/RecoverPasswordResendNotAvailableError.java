package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RecoverPasswordResendNotAvailableError",
        description = "Password recovery email resend is not currently available."
)
public abstract class RecoverPasswordResendNotAvailableError extends BaseError<Void> {

    @Override
    @Schema(
            example = "RESEND_NOT_AVAILABLE",
            allowableValues = {"RESEND_NOT_AVAILABLE"}
    )
    public String code() {
        return "RESEND_NOT_AVAILABLE";
    }
}
