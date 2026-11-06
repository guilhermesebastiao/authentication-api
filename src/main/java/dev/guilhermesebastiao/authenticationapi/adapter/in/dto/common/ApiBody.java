package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ApiBody",
        description = "Standard successful API response"
)
public record ApiBody<T>(
        @Schema(
                description = "Indicates whether the request was successful",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Boolean success,

        @Schema(
                description = "Response data",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        T data
) {
    public static ApiBody<Void> of() {
        return new ApiBody<>(true, null);
    }

    public static <T> ApiBody<T> of(T data) {
        return new ApiBody<T>(true, data);
    }
}
