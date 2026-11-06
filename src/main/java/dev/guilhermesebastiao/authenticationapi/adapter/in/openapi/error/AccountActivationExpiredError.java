package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountActivationExpiredError",
        description = "The account activation token has expired."
)
public abstract class AccountActivationExpiredError extends BaseError<Void> {

    @Override
    @Schema(
            example = "ACCOUNT_ACTIVATION_EXPIRED",
            allowableValues = {"ACCOUNT_ACTIVATION_EXPIRED"}
    )
    public String code() {
        return "ACCOUNT_ACTIVATION_EXPIRED";
    }
}
