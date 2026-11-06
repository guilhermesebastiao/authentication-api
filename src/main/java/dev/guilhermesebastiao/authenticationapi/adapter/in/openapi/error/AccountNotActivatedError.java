package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountNotActivatedError",
        description = "The account has not been activated."
)
public abstract class AccountNotActivatedError extends BaseError<Object> {

    @Override
    @Schema(
            example = "ACCOUNT_NOT_ACTIVATED",
            allowableValues = {"ACCOUNT_NOT_ACTIVATED"}
    )
    public String code() {
        return "ACCOUNT_NOT_ACTIVATED";
    }
}
