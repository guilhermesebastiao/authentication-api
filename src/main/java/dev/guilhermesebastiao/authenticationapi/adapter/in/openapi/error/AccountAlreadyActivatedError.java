package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountAlreadyActivatedError",
        description = "The account is already activated."
)
public abstract class AccountAlreadyActivatedError extends BaseError<Void> {

    @Override
    @Schema(
            example = "ACCOUNT_ALREADY_ACTIVATED",
            allowableValues = {"ACCOUNT_ALREADY_ACTIVATED"}
    )
    public String code() {
        return "ACCOUNT_ALREADY_ACTIVATED";
    }
}
