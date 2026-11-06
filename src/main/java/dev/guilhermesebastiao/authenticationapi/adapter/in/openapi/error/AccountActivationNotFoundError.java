package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountActivationNotFoundError",
        description = "The account activation record could not be found."
)
public abstract class AccountActivationNotFoundError extends BaseError<Void> {

    @Override
    @Schema(
            example = "ACCOUNT_ACTIVATION_NOT_FOUND",
            allowableValues = {"ACCOUNT_ACTIVATION_NOT_FOUND"}
    )
    public String code() {
        return "ACCOUNT_ACTIVATION_NOT_FOUND";
    }
}
