package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountDisabledError",
        description = "The account is disabled."
)
public abstract class AccountDisabledError extends BaseError<Void> {

    @Override
    @Schema(
            example = "ACCOUNT_DISABLED",
            allowableValues = {"ACCOUNT_DISABLED"}
    )
    public String code() {
        return "ACCOUNT_DISABLED";
    }
}
