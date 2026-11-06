package dev.guilhermesebastiao.authenticationapi.application.result;

import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;

public record CreateRefreshResult(
        String refreshToken,
        Refresh refresh
) {
}
