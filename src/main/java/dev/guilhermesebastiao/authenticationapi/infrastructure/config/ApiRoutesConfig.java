package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

@NoArgsConstructor
public final class ApiRoutesConfig {
    public static final String[] PUBLIC = {
            "/auth/sign-in",
            "/auth/google/sign-in",
            "/auth/sign-up",
            "/auth/refresh",
            "/recovery-password/**",
            "/forgot-password/**",
            "/account-activation/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public static boolean isPublic(String path) {
        return Arrays.stream(PUBLIC)
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }
}
