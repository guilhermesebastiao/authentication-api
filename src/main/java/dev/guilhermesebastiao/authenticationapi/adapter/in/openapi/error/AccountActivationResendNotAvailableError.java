package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountActivationResendNotAvailableError",
        description = "Account activation email resend is not currently available."
)
public abstract class AccountActivationResendNotAvailableError extends BaseError<Void> {

    @Override
    @Schema(
            example = "RESEND_NOT_AVAILABLE",
            allowableValues = {"RESEND_NOT_AVAILABLE"}
    )
    public String code() {
        return "RESEND_NOT_AVAILABLE";
    }
}
