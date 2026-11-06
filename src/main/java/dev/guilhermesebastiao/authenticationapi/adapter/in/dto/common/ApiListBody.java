package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

import java.util.List;

public record ApiListBody<T>(
        List<T> items,
        int page,
        int size,
        long total,
        int totalPages
) {
    public static <T> ApiListBody<T> of(List<T> items, int page, int size, long total, int totalPages) {
        return new ApiListBody<T>(items, page, size, total, totalPages);
    }
}
