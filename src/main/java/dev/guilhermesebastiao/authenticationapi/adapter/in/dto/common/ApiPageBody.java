package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;


import java.util.List;

public record ApiPageBody<T>(
        List<T> results,
        int page,
        int size,
        long totalItems,
        int totalPages
) {
    public static <T> ApiPageBody<T> of(List<T> results, int page, int size, long totalItems, int totalPages) {
        return new ApiPageBody<T>(results, page, size, totalItems, totalPages);
    }
}
