package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.error;

import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseError;
import dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common.BaseFieldError;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ValidationError",
        description = "The request contains one or more fields with invalid values."
)
public abstract class ValidationError extends BaseError<BaseFieldError> {

    @Override
    @Schema(
            example = "VALIDATION_ERROR",
            allowableValues = {"VALIDATION_ERROR"}
    )
    public String code() {
        return "VALIDATION_ERROR";
    }
}
