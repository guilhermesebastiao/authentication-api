package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PaginationParams(
        @Min(value = 1)
        Integer page,

        @Min(value = 1)
        @Max(value = 50)
        Integer size
) {
    public PaginationParams {
        page = page == null ? 1 : page;
        size = size == null ? 20 : size;
    }
}
