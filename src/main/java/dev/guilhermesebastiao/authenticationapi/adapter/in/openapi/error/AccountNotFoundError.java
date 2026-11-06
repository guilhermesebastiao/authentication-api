package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountNotFoundError",
        description = "The account could not be found."
)
public abstract class AccountNotFoundError extends BaseError<Void> {

    @Override
    @Schema(
            example = "ACCOUNT_NOT_FOUND",
            allowableValues = {"ACCOUNT_NOT_FOUND"}
    )
    public String code() {
        return "ACCOUNT_NOT_FOUND";
    }
}
