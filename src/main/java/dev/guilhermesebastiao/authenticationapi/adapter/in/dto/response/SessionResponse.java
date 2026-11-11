package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;

import java.time.Instant;
import java.util.UUID;

public record SessionResponse(
        UUID id,
        String name,
        DeviceType type,
        Instant lastSeenAt,
        String location,
        String ipAddress,
        boolean isCurrent,
        Instant createdAt
) {
}
