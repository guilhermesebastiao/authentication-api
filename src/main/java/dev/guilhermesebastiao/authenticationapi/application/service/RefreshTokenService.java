package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.RefreshTokenInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateRefreshUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RefreshTokenUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.ValidateSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.JwtTokenPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AuthOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateRefreshOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.JwtValidationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.JwtValidationStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RefreshTokenInvalidException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RefreshTokenNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RefreshTokenRevokedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.UnauthorizedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.Set;

public class RefreshTokenService implements RefreshTokenUseCase {
    private final ValidateSessionUseCase validateSessionUseCase;
    private final CreateRefreshUseCase createRefreshUseCase;
    private final RefreshRepositoryPort refreshRepository;
    private final SessionRepositoryPort sessionRepository;
    private final SecureHasherPort secureHasher;
    private final JwtTokenPort jwtToken;

    public RefreshTokenService(
            ValidateSessionUseCase validateSessionUseCase,
            CreateRefreshUseCase createRefreshUseCase,
            RefreshRepositoryPort refreshRepository,
            SessionRepositoryPort sessionRepository,
            SecureHasherPort secureHasher,
            JwtTokenPort jwtToken
    ) {
        this.validateSessionUseCase = validateSessionUseCase;
        this.createRefreshUseCase = createRefreshUseCase;
        this.refreshRepository = refreshRepository;
        this.sessionRepository = sessionRepository;
        this.secureHasher = secureHasher;
        this.jwtToken = jwtToken;
    }

    @Override
    public AuthOutput execute(RefreshTokenInput input) {
        Session session = validateSessionUseCase.execute(input.sessionToken());

        JwtValidationOutput jwtResult = jwtToken.validate(
                input.accessToken(),
                input.sessionToken()
        );

        boolean jwtIsExpired = validateJwtStatus(jwtResult.status());

        if (!jwtIsExpired) {
            return new AuthOutput(input.accessToken(), input.refreshToken(), input.sessionToken());
        }

        Refresh previousRefresh = consumeAndValidateRefresh(input.refreshToken(), session);

        CreateRefreshOutput refreshResult = createRefreshUseCase.execute(session);

        previousRefresh.setReplacedBy(refreshResult.refresh());

        refreshRepository.save(previousRefresh);

        updateSession(session);

        String accessToken = jwtToken.generate(session.getAccount(), input.sessionToken());

        return new AuthOutput(accessToken, refreshResult.refreshToken(), input.sessionToken());
    }

    private void updateSession(Session session) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(7, ChronoUnit.DAYS);

        session.setLastSeenAt(now);
        session.setExpiresAt(expiresAt);

        sessionRepository.save(session);
    }

    private boolean validateJwtStatus(JwtValidationStatus status) {
        Set<JwtValidationStatus> allowedStatuses = EnumSet.of(
                JwtValidationStatus.VALID,
                JwtValidationStatus.EXPIRED
        );

        if (!allowedStatuses.contains(status)) {
            throw new UnauthorizedException();
        }

        return status == JwtValidationStatus.EXPIRED;
    }

    private Refresh consumeAndValidateRefresh(String refreshToken, Session session) {
        String refreshTokenHash = secureHasher.hash(refreshToken);

        Refresh refresh = refreshRepository.findByRefreshTokenHash(refreshTokenHash)
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (refresh.getRevokedAt() != null) {
            throw new RefreshTokenRevokedException();
        }

        if (!refresh.getSession().getId().equals(session.getId())) {
            throw new RefreshTokenInvalidException();
        }

        refresh.setRevokedAt(Instant.now());

        return refresh;
    }
}
