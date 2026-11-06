package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.GoogleProperties;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GoogleAuthConfig {
    private final GoogleProperties properties;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() throws GeneralSecurityException, IOException {
        return new GoogleIdTokenVerifier
                .Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                .setAudience(List.of(properties.clientId()))
                .build();
    }

    @Bean
    public HttpTransport googleHttpTransport()
            throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public JsonFactory googleJsonFactory() {
        return GsonFactory.getDefaultInstance();
    }
}
