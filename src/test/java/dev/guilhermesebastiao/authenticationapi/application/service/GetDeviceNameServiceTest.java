package dev.guilhermesebastiao.authenticationapi.application.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GetDeviceNameServiceTest {
    private final GetDeviceNameService service = new GetDeviceNameService();

    @Test void returnsUnknownForNullOrBlankAgent() {
        assertEquals("Dispositivo desconhecido", service.execute(null));
        assertEquals("Dispositivo desconhecido", service.execute("  "));
    }

    @Test void identifiesBrowserAndOperatingSystem() {
        assertEquals("Chrome on Windows 10/11", service.execute("Mozilla/5.0 (Windows NT 10.0; Win64) AppleWebKit/537.36 Chrome/120.0.0.0 Safari/537.36"));
        assertEquals("Firefox on Android 14", service.execute("Mozilla/5.0 (Android 14; Mobile) Firefox/120.0"));
    }

    @Test void returnsKnownPartWhenOtherPartIsUnknown() {
        assertEquals("Linux", service.execute("Linux"));
        assertEquals("Chrome", service.execute("Chrome/120"));
    }
}
