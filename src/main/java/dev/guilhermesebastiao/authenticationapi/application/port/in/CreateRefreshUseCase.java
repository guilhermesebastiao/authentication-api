package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.CreateRefreshResult;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

public interface CreateRefreshUseCase {
    CreateRefreshResult execute(Session session);
}
