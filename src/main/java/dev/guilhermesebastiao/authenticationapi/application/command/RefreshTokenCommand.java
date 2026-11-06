package dev.guilhermesebastiao.authenticationapi.application.command;

public record RefreshTokenCommand(
        String accessToken,
        String refreshToken,
        String sessionToken
) {
}
