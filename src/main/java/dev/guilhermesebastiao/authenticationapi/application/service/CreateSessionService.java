package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.IpGeolocationPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateSessionOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.IpLocationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateSessionService implements CreateSessionUseCase {
    private static final int SESSION_TOKEN_SIZE = 32;

    private final SecureRandomGeneratorPort secureRandomGenerator;
    private final SessionRepositoryPort sessionRepository;
    private final IpGeolocationPort ipGeolocation;
    private final SecureHasherPort secureHasher;

    public CreateSessionService(
            SecureRandomGeneratorPort secureRandomGenerator,
            SessionRepositoryPort sessionRepository,
            IpGeolocationPort ipGeolocation,
            SecureHasherPort secureHasher
    ) {
        this.secureRandomGenerator = secureRandomGenerator;
        this.sessionRepository = sessionRepository;
        this.ipGeolocation = ipGeolocation;
        this.secureHasher = secureHasher;
    }

    @Override
    public CreateSessionOutput execute(Account account, String userAgent, String ipAddress) {
        String sessionToken = secureRandomGenerator.generate(SESSION_TOKEN_SIZE);
        String sessionTokenHash = secureHasher.hash(sessionToken);

        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        DeviceType type = getDeviceType(userAgent);

        String location = createLocation(ipAddress);

        Session sessionEntity = new Session();

        sessionEntity.setAccount(account);
        sessionEntity.setExpiresAt(expiresAt);
        sessionEntity.setIpAddress(ipAddress);
        sessionEntity.setLastSeenAt(Instant.now());
        sessionEntity.setType(type);
        sessionEntity.setUserAgent(userAgent);
        sessionEntity.setSessionTokenHash(sessionTokenHash);
        sessionEntity.setLocation(location);

        Session session = sessionRepository.save(sessionEntity);

         return new CreateSessionOutput(sessionToken, session);
    }

    private String createLocation(String ipAddress) {
        Optional<IpLocationOutput> findLocation = ipGeolocation.findLocation(ipAddress);

        if (findLocation.isEmpty()) {
            return "Localização desconhecida";
        }

        IpLocationOutput location = findLocation.get();

        return Stream.of(location.city(), location.country())
                .filter(value -> value != null && !value.isBlank())
                .map(this::capitalize)
                .collect(Collectors.joining(", "));
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        String normalized = value.trim();

        return normalized.substring(0, 1).toUpperCase(Locale.ROOT) + normalized.substring(1).toLowerCase(Locale.ROOT);
    }

    private DeviceType getDeviceType(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return DeviceType.OTHER;
        }

        String normalizedUserAgent = userAgent.toLowerCase();

        return Arrays.stream(DeviceType.values())
                .filter(type -> type != DeviceType.OTHER)
                .filter(type -> type.matches(normalizedUserAgent))
                .findFirst()
                .orElse(DeviceType.OTHER);
    }
}
