package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(
        name = "SuccessWithoutData",
        description = "Successful response without response data"
)
public abstract class SuccessWithoutData {

    @Schema(
            description = "Response status",
            example = "success",
            allowableValues = {"success"}
    )
    public String status;

    @Schema(
            description = "Response data",
            example = "null",
            nullable = true
    )
    public Object data;
}