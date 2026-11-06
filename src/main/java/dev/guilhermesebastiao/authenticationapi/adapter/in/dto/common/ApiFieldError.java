package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

public record ApiFieldError(
        String field,
        String code
) {
    public static ApiFieldError of(String field, String code) {
        return new ApiFieldError(field, code);
    }
}
