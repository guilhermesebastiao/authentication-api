package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.IpGeolocationPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SessionRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.result.CreateSessionResult;
import dev.guilhermesebastiao.authenticationapi.application.result.IpLocationResult;
import dev.guilhermesebastiao.authenticationapi.domain.enums.DeviceType;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CreateSessionServiceTest {

    @Mock
    private SecureRandomGeneratorPort secureRandomGenerator;

    @Mock
    private SessionRepositoryPort sessionRepository;

    @Mock
    private IpGeolocationPort ipGeolocation;

    @Mock
    private SecureHasherPort secureHasher;

    @InjectMocks
    private CreateSessionService service;

    @Test
    @DisplayName("Should create and save a desktop session with its location")
    void givenAccountDesktopUserAgentAndKnownLocation_whenCreateSession_thenSessionIsSavedAndReturned() {
        Account account = new Account();
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";
        String ipAddress = "192.168.0.10";
        String sessionToken = "session-token";
        String sessionTokenHash = "session-token-hash";
        Instant beforeExecution = Instant.now();

        given(secureRandomGenerator.generate(32))
                .willReturn(sessionToken);

        given(secureHasher.hash(sessionToken))
                .willReturn(sessionTokenHash);

        given(ipGeolocation.findLocation(ipAddress))
                .willReturn(Optional.of(new IpLocationResult("brAzil", "sao paulo")));

        given(sessionRepository.save(any(Session.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        CreateSessionResult result = service.execute(account, userAgent, ipAddress);
        Session savedSession = result.session();
        Instant afterExecution = Instant.now();

        assertEquals(sessionToken, result.sessionToken());
        assertNotNull(savedSession);
        assertSame(account, savedSession.getAccount());
        assertEquals(sessionTokenHash, savedSession.getSessionTokenHash());
        assertEquals(DeviceType.DESKTOP, savedSession.getType());
        assertEquals(userAgent, savedSession.getUserAgent());
        assertEquals(ipAddress, savedSession.getIpAddress());
        assertEquals("Sao paulo, Brazil", savedSession.getLocation());
        assertNotNull(savedSession.getLastSeenAt());
        assertFalse(savedSession.getLastSeenAt().isBefore(beforeExecution));
        assertFalse(savedSession.getLastSeenAt().isAfter(afterExecution));
        assertNotNull(savedSession.getExpiresAt());
        assertTrue(savedSession.getExpiresAt().isAfter(beforeExecution.plusSeconds(6 * 24 * 60 * 60)));
        assertTrue(savedSession.getExpiresAt().isBefore(afterExecution.plusSeconds(8 * 24 * 60 * 60)));

        then(secureRandomGenerator).should().generate(32);
        then(secureHasher).should().hash(sessionToken);
        then(ipGeolocation).should().findLocation(ipAddress);
        then(sessionRepository).should().save(savedSession);
    }

    @Test
    @DisplayName("Should use an unknown location when geolocation data is unavailable")
    void givenUnavailableLocation_whenCreateSession_thenUnknownLocationIsStored() {
        Account account = new Account();
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64)";
        String ipAddress = "203.0.113.10";

        given(secureRandomGenerator.generate(32))
                .willReturn("session-token");

        given(secureHasher.hash("session-token"))
                .willReturn("session-token-hash");

        given(ipGeolocation.findLocation(ipAddress))
                .willReturn(Optional.empty());

        given(sessionRepository.save(any(Session.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        CreateSessionResult result = service.execute(account, userAgent, ipAddress);

        assertEquals("Localização desconhecida", result.session().getLocation());
        then(ipGeolocation).should().findLocation(ipAddress);
        then(sessionRepository).should().save(result.session());
    }

    @Test
    @DisplayName("Should normalize the available part of a partial location")
    void givenPartialLocation_whenCreateSession_thenAvailableLocationPartIsNormalizedAndStored() {
        Account account = new Account();
        String ipAddress = "198.51.100.10";

        given(secureRandomGenerator.generate(32))
                .willReturn("session-token");

        given(secureHasher.hash("session-token"))
                .willReturn("session-token-hash");

        given(ipGeolocation.findLocation(ipAddress))
                .willReturn(Optional.of(new IpLocationResult(null, "  sAO PAULO ")));

        given(sessionRepository.save(any(Session.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        CreateSessionResult result = service.execute(account, "unknown-agent", ipAddress);

        assertEquals("Sao paulo", result.session().getLocation());
        then(ipGeolocation).should().findLocation(ipAddress);
        then(sessionRepository).should().save(result.session());
    }

    @Test
    @DisplayName("Should classify a missing user agent as another device type")
    void givenMissingUserAgent_whenCreateSession_thenDeviceTypeIsOther() {
        Account account = new Account();
        String ipAddress = "203.0.113.20";

        given(secureRandomGenerator.generate(32))
                .willReturn("session-token");

        given(secureHasher.hash("session-token"))
                .willReturn("session-token-hash");

        given(ipGeolocation.findLocation(ipAddress))
                .willReturn(Optional.empty());

        given(sessionRepository.save(any(Session.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        CreateSessionResult result = service.execute(account, null, ipAddress);

        assertEquals(DeviceType.OTHER, result.session().getType());
        then(sessionRepository).should().save(result.session());
    }

    @Test
    @DisplayName("Should classify an unrecognized user agent as another device type")
    void givenUnrecognizedUserAgent_whenCreateSession_thenDeviceTypeIsOther() {
        Account account = new Account();
        String ipAddress = "198.51.100.20";

        given(secureRandomGenerator.generate(32))
                .willReturn("session-token");

        given(secureHasher.hash("session-token"))
                .willReturn("session-token-hash");

        given(ipGeolocation.findLocation(ipAddress))
                .willReturn(Optional.empty());

        given(sessionRepository.save(any(Session.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        CreateSessionResult result = service.execute(account, "curl/8.0", ipAddress);

        assertEquals(DeviceType.OTHER, result.session().getType());
        then(sessionRepository).should().save(result.session());
    }
}
