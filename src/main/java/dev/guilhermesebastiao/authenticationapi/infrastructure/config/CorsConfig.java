package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.AppProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class CorsConfig {
    private final AppProperties properties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedMethods = List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
        List<String> allowedOrigins = List.of(properties.frontendUrl());
        List<String> allowedHeaders = List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Session"
        );

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}