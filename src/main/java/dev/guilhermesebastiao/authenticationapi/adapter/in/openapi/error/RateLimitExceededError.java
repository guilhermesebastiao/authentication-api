package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RateLimitExceededError",
        description = "The request rate limit has been exceeded."
)
public abstract class RateLimitExceededError extends BaseError<Void> {

    @Override
    @Schema(
            example = "RATE_LIMIT_EXCEEDED",
            allowableValues = {"RATE_LIMIT_EXCEEDED"}
    )
    public String code() {
        return "RATE_LIMIT_EXCEEDED"; }
}
