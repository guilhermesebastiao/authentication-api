package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response;

import java.util.Set;
import java.util.UUID;

public record AccountResponse(
    UUID id,
    String email,
    Set<String> roles
) {
}
