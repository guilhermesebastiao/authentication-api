package dev.guilhermesebastiao.authenticationapi.application.dto.input;

public record RefreshTokenInput(
        String accessToken,
        String refreshToken,
        String sessionToken
) {
}
