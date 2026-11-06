package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;

import java.time.Instant;
import java.util.UUID;

public record SessionResponse(
        UUID id,
        DeviceType type,
        Instant lastSeenAt,
        String location,
        boolean isCurrent,
        Instant createdAt
) {
}
