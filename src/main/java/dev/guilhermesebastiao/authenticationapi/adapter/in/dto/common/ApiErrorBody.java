package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ApiErrorBody",
        description = "Standard error response returned by the API"
)
public record ApiErrorBody(
        @Schema(
                description = "Indicates whether the request was successful",
                example = "false",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Boolean success,

        @Schema(
                description = "Short description of the error",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String title,

        @Schema(
                description = "HTTP status code",
                example = "500",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        int status,

        @Schema(
                description = "Detailed description of the error",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String detail,

        @Schema(
                description = "Unique code that identifies the error",
                example = "string",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        DomainErrorCode code
) {
    public static ApiErrorBody of(String title, int status, String detail, DomainErrorCode code) {
        return new ApiErrorBody(false, title, status, detail, code);
    }
}
