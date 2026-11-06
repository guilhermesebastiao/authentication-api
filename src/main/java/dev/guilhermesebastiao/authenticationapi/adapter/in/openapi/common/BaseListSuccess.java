package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(
        name = "BaseListBodySuccess",
        description = "Paginated list response"
)
public abstract class BaseListSuccess<T> {

    @ArraySchema(
            arraySchema = @Schema(
                    description = "List of items returned in the current page"
            )
    )
    public List<T> items;

    @Schema(
            description = "Current page number",
            example = "1",
            minimum = "1"
    )
    public int page;

    @Schema(
            description = "Maximum number of items per page",
            example = "20",
            maximum = "50",
            minimum = "1"
    )
    public int size;

    @Schema(
            description = "Total number of items available",
            example = "125",
            minimum = "0"
    )
    public long total;

    @Schema(
            description = "Total number of pages available",
            example = "7",
            minimum = "0"
    )
    public int totalPages;
}
