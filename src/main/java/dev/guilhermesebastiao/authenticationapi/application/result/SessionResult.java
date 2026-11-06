package dev.guilhermesebastiao.authenticationapi.application.result;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;

import java.time.Instant;
import java.util.UUID;

public record SessionResult(
        UUID id,
        DeviceType type,
        Instant lastSeenAt,
        String location,
        boolean isCurrent,
        Instant createdAt
) {
}
