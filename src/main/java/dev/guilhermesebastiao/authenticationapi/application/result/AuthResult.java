package dev.guilhermesebastiao.authenticationapi.application.result;

public record AuthResult(
        String accessToken,
        String refreshToken,
        String sessionToken
) {
}
