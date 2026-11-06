package dev.guilhermesebastiao.authenticationapi.application.dto.output;

import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;

public record CreateRefreshOutput(
        String refreshToken,
        Refresh refresh
) {
}
