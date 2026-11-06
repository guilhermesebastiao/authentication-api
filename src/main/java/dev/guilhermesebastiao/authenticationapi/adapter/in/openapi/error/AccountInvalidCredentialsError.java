package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountInvalidCredentialsError",
        description = "The provided account credentials are invalid."
)
public abstract class AccountInvalidCredentialsError extends BaseError<Void> {

    @Override
    @Schema(
            example = "ACCOUNT_INVALID_CREDENTIALS",
            allowableValues = {"ACCOUNT_INVALID_CREDENTIALS"}
    )
    public String code() {
        return "ACCOUNT_INVALID_CREDENTIALS";
    }
}
