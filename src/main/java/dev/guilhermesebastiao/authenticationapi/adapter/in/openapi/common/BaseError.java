package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(
        name = "BaseError",
        description = "Base response structure returned when an error occurs"
)
public abstract class BaseError<T> {

    @Schema(
            description = "Response status",
            example = "error",
            allowableValues = {"error"}
    )
    private String status;

    @JsonProperty("code")
    @Schema(
            description = "Unique code that identifies the error",
            example = "INTERNAL_SERVER_ERROR",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    public abstract String code();

    @Schema(
            description = "Additional details about the error",
            nullable = true
    )
    private T details;
}
