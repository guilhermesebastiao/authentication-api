package dev.guilhermesebastiao.authenticationapi.application.dto.common;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        long totalItems,
        int totalPages,
        int page,
        int size
) {
}