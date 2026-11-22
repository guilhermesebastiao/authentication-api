package dev.guilhermesebastiao.authenticationapi.application.dto.common;

import java.util.List;

public record PageBody<T>(
        List<T> content,
        long totalItems,
        int totalPages,
        int page,
        int size
) {
}