package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountAlreadyExistsError",
        description = "An account with the provided identifier already exists."
)
public abstract class AccountAlreadyExistsError extends BaseError<Void> {

    @Override
    @Schema(
            example = "ACCOUNT_ALREADY_EXISTS",
            allowableValues = {"ACCOUNT_ALREADY_EXISTS"}
    )
    public String code() {
        return "ACCOUNT_ALREADY_EXISTS";
    }
}
