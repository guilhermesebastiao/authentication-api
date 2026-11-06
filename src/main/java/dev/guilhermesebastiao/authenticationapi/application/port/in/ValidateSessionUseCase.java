package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

public interface ValidateSessionUseCase {
    Session execute(String sessionToken);
}
