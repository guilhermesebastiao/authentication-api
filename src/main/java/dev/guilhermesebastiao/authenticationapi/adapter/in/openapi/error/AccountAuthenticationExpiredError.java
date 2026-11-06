package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountAuthenticationExpiredError",
        description = "Account authentication has expired."
)
public abstract class AccountAuthenticationExpiredError extends BaseError<Void> {

    @Override
    @Schema(
            example = "ACCOUNT_EXPIRED",
            allowableValues = {"ACCOUNT_EXPIRED"}
    )
    public String code() {
        return "ACCOUNT_EXPIRED";
    }
}
