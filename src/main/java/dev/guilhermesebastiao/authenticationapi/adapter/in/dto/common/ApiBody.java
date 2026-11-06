package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

public record ApiBody<T>(
        String status,
        T data
) {
    public static ApiBody<Void> of() {
        return new ApiBody<>("success", null);
    }

    public static <T> ApiBody<T> of(T data) {
        return new ApiBody<T>("success", data);
    }
}
