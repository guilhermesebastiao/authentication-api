
package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.GetDeviceNameUseCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetDeviceNameService implements GetDeviceNameUseCase {

    private static final String UNKNOWN = "Desconhecido";

    private static final String UNKNOWN_DEVICE = "Dispositivo desconhecido";

    private static final Pattern ANDROID_VERSION = Pattern.compile("Android[ /]([\\d.]+)");

    private static final Pattern IOS_VERSION = Pattern.compile("(?:CPU (?:iPhone )?OS|iPhone OS) ([\\d_]+)");

    private static final Pattern MACOS_VERSION = Pattern.compile("Mac OS X ([\\d_.]+)");

    private static final Pattern UBUNTU_VERSION = Pattern.compile("Ubuntu[/ ]([\\d.]+)");

    @Override
    public String execute(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return UNKNOWN_DEVICE;
        }

        String browser = getBrowser(userAgent);
        String operatingSystem = getOperatingSystem(userAgent);

        if (UNKNOWN.equals(browser) && UNKNOWN.equals(operatingSystem)) {
            return UNKNOWN_DEVICE;
        }

        if (UNKNOWN.equals(browser)) {
            return operatingSystem;
        }

        if (UNKNOWN.equals(operatingSystem)) {
            return browser;
        }

        return "%s on %s".formatted(browser, operatingSystem);
    }

    private String getBrowser(String userAgent) {
        if (containsAny(userAgent, "Edg/", "EdgA/", "EdgiOS/")) {
            return "Edge";
        }

        if (containsAny(userAgent, "OPR/", "Opera/", "OPT/")) {
            return "Opera";
        }

        if (userAgent.contains("SamsungBrowser/")) {
            return "Samsung Internet";
        }

        if (userAgent.contains("Vivaldi/")) {
            return "Vivaldi";
        }

        if (userAgent.contains("Brave/")) {
            return "Brave";
        }

        if (containsAny(userAgent, "Firefox/", "FxiOS/")) {
            return "Firefox";
        }

        if (containsAny(userAgent, "Chrome/", "CriOS/")) {
            return "Chrome";
        }

        if (userAgent.contains("Safari/")) {
            return "Safari";
        }

        return UNKNOWN;
    }

    private String getOperatingSystem(String userAgent) {
        if (userAgent.contains("Windows NT 10.0")) {
            return "Windows 10/11";
        }

        if (userAgent.contains("Windows NT 6.3")) {
            return "Windows 8.1";
        }

        if (userAgent.contains("Windows NT 6.2")) {
            return "Windows 8";
        }

        if (userAgent.contains("Windows NT 6.1")) {
            return "Windows 7";
        }

        if (userAgent.contains("Windows")) {
            return "Windows";
        }

        if (userAgent.contains("Android")) {
            return withVersion("Android", extractVersion(userAgent, ANDROID_VERSION));
        }

        if (containsAny(userAgent, "iPhone", "iPad", "iPod")) {
            return withVersion(
                    "iOS",
                    extractVersion(userAgent, IOS_VERSION).replace('_', '.')
            );
        }

        if (userAgent.contains("Mac OS X")) {
            return withVersion(
                    "macOS",
                    extractVersion(userAgent, MACOS_VERSION).replace('_', '.')
            );
        }

        if (userAgent.contains("Ubuntu")) {
            return withVersion(
                    "Ubuntu",
                    extractVersion(userAgent, UBUNTU_VERSION)
            );
        }

        if (userAgent.contains("Fedora")) {
            return "Fedora";
        }

        if (userAgent.contains("Debian")) {
            return "Debian";
        }

        if (userAgent.contains("CrOS")) {
            return "ChromeOS";
        }

        if (userAgent.contains("Linux")) {
            return "Linux";
        }

        return UNKNOWN;
    }

    private String extractVersion(String userAgent, Pattern pattern) {
        Matcher matcher = pattern.matcher(userAgent);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String withVersion(String name, String version) {
        return version.isBlank() ? name : "%s %s".formatted(name, version);
    }

    private boolean containsAny(String value, String... terms) {
        for (String term : terms) {
            if (value.contains(term)) {
                return true;
            }
        }

        return false;
    }
}
