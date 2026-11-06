package dev.guilhermesebastiao.authenticationapi.domain.enums;

import java.util.Arrays;

public enum DeviceType {
    TABLET("ipad", "tablet"),
    MOBILE("mobile", "iphone", "ipod", "android"),
    DESKTOP("windows", "macintosh", "linux", "x11"),
    OTHER();

    private final String[] identifiers;

    DeviceType(String... identifiers) {
        this.identifiers = identifiers;
    }

    public boolean matches(String userAgent) {
        return Arrays.stream(identifiers).anyMatch(userAgent::contains);
    }
}