package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateRefreshUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateRefreshOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

public class CreateRefreshService implements CreateRefreshUseCase {
    private static final int REFRESH_TOKEN_SIZE = 32;

    private final SecureRandomGeneratorPort secureRandomGenerator;
    private final RefreshRepositoryPort refreshRepository;
    private final SecureHasherPort secureHasher;

    public CreateRefreshService(
            SecureRandomGeneratorPort secureRandomGenerator,
            RefreshRepositoryPort refreshRepository,
            SecureHasherPort secureHasher
    ) {
        this.secureRandomGenerator = secureRandomGenerator;
        this.refreshRepository = refreshRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public CreateRefreshOutput execute(Session session) {
        String refreshToken = secureRandomGenerator.generate(REFRESH_TOKEN_SIZE);

        Refresh refresh = createRefresh(session, refreshToken);

        Refresh savedRefresh = refreshRepository.save(refresh);

        return new CreateRefreshOutput(refreshToken, savedRefresh);
    }

    private Refresh createRefresh(Session session, String token) {
        String refreshTokenHash = secureHasher.hash(token);

        Refresh refresh = new Refresh();

        refresh.setSession(session);
        refresh.setRefreshTokenHash(refreshTokenHash);

        return refresh;
    }
}
