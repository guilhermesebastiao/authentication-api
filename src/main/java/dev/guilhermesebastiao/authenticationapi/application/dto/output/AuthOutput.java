package dev.guilhermesebastiao.authenticationapi.application.dto.output;

public record AuthOutput(
        String accessToken,
        String refreshToken,
        String sessionToken
) {
}
