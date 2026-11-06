package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public record ApiBodyError<T>(
        String status,
        String code,
        T details
) {
    public static <T> ApiBodyError<T> of(DomainErrorCode code) {
        return new ApiBodyError<>("error", code.name(), null);
    }

    public static <T> ApiBodyError<T> of(DomainErrorCode code, T details) {
        return new ApiBodyError<>("error", code.name(), details);
    }
}
