package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "IncorrectOtpError",
        description = "The provided one-time password is incorrect."
)
public abstract class IncorrectOtpError extends BaseError<Void> {

    @Override
    @Schema(
            example = "INCORRECT_OTP",
            allowableValues = {"INCORRECT_OTP"}
    )
    public String code() {
        return "INCORRECT_OTP";
    }
}
