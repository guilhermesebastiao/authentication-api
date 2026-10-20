package br.com.guisebastiao.authenticationapi.infrastructure.config;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ApiRoutesConfig {
    public static final String[] PUBLIC = {
            "/auth/sign-in",
            "/auth/google/sign-in",
            "/auth/sign-up",
            "/auth/refresh",
            "/password-recovery/**",
            "/account-activation/**",
            "/actuator/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
