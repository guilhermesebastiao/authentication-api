package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.*;

public record RevokeSessionsRequest(
        @NotEmpty(message = REQUIRED)
        List<@NotNull(message = REQUIRED) UUID> sessionsIds
) {
}
