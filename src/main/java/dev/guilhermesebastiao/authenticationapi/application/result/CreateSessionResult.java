package dev.guilhermesebastiao.authenticationapi.application.result;

import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

public record CreateSessionResult(
        String sessionToken,
        Session session
) {
}
