package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(
        name = "BaseBodySuccess",
        description = "Standard successful response body"
)
public abstract class BaseSuccess<T> {

    @Schema(
            description = "Response status",
            example = "success",
            allowableValues = {"success"}
    )
    public String status;

    @Schema(
            description = "Response data"
    )
    public T data;
}
