package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

public record ApiFieldError(
        String field,
        String error
) {
}
