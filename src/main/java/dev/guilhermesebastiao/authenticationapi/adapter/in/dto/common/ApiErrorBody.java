package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public record ApiErrorBody(
        String title,
        String message,
        int status,
        DomainErrorCode code,
        Object details
) {
    public static ApiErrorBody of(String title, String message, int status, DomainErrorCode code) {
        return new ApiErrorBody(title, message, status, code, null);
    }

    public static ApiErrorBody of(String title, String message, int status, DomainErrorCode code, Object details) {
        return new ApiErrorBody(title, message, status, code, details);
    }
}
