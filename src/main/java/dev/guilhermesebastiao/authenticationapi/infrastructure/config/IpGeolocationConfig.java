package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class IpGeolocationConfig {

    @Bean
    public RestClient ipGeolocationRestClient() {
        return RestClient.builder().baseUrl("https://ipwho.is").build();
    }
}
