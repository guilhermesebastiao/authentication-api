package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshRepositoryPort {
    Refresh save(Refresh refresh);

    void save(List<Refresh> refreshes);

    Optional<Refresh> findByRefreshBySessionId(UUID sessionId);

    Optional<Refresh> findByRefreshTokenHash(String refreshTokenHash);

    List<Refresh> findAllBySessionIdsAndNotRevoked(List<UUID> sessionIds);
}
