package dev.guilhermesebastiao.authenticationapi.application.dto.output;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;

import java.time.Instant;
import java.util.UUID;

public record SessionOutput(
        UUID id,
        DeviceType type,
        Instant lastSeenAt,
        String location,
        boolean isCurrent,
        Instant createdAt
) {
}
