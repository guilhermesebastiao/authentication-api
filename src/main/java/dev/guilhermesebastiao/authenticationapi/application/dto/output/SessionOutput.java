package dev.guilhermesebastiao.authenticationapi.application.dto.output;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;

import java.time.Instant;
import java.util.UUID;

public record SessionOutput(
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
