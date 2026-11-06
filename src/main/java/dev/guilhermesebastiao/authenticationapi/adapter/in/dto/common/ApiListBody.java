package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "ApiListBody",
        description = "Standard paginated list response"
)
public record ApiListBody<T>(
        @Schema(
                description = "List of items returned in the current page",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        List<T> items,

        @Schema(
                description = "Current page number",
                example = "2",
                minimum = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        int page,

        @Schema(
                description = "Maximum number of items per page",
                example = "20",
                minimum = "1",
                maximum = "50",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        int size,

        @Schema(
                description = "Total number of items across all pages",
                example = "50",
                minimum = "0",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        long totalItems,

        @Schema(
                description = "Total number of available pages",
                example = "3",
                minimum = "0",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        int totalPages
) {
    public static <T> ApiListBody<T> of(List<T> items, int page, int size, long totalItems, int totalPages) {
        return new ApiListBody<T>(items, page, size, totalItems, totalPages);
    }
}
