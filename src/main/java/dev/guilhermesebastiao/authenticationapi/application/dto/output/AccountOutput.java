package dev.guilhermesebastiao.authenticationapi.application.dto.output;

import java.util.Set;
import java.util.UUID;

public record AccountOutput(
        UUID id,
        String email,
        Set<String> roles
) {
}
