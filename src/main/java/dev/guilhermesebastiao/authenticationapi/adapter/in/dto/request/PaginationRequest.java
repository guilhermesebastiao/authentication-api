package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.*;

public record PaginationRequest(
        @Min(value = 1, message = INVALID_MIN_NUMBER)
        Integer page,

        @Min(value = 1, message = INVALID_MIN_NUMBER)
        @Max(value = 50, message = INVALID_MAX_NUMBER)
        Integer size
) {
    public PaginationRequest {
        page = page == null ? 1 : page;
        size = size == null ? 20 : size;
    }
}
