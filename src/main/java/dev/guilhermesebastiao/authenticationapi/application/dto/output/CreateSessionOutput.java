package dev.guilhermesebastiao.authenticationapi.application.dto.output;

import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

public record CreateSessionOutput(
        String sessionToken,
        Session session
) {
}
