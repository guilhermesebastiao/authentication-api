package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SessionNotBelongToAccountError",
        description = "The session does not belong to the requested account."
)
public abstract class SessionNotBelongToAccountError extends BaseError<Void> {

    @Override
    @Schema(
            example = "SESSION_NOT_BELONG_TO_ACCOUNT",
            allowableValues = {"SESSION_NOT_BELONG_TO_ACCOUNT"}
    )
    public String code() {
        return "SESSION_NOT_BELONG_TO_ACCOUNT";
    }
}
