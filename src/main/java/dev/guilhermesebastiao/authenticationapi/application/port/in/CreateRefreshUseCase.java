package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateRefreshOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

public interface CreateRefreshUseCase {
    CreateRefreshOutput execute(Session session);
}
