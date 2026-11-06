package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String sessionToken
) {
}
