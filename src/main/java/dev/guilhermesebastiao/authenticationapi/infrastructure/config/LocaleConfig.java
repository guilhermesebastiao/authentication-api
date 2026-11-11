package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @PostConstruct
    public void configureLocale() {
        Locale.setDefault(Locale.of("pt", "BR"));
    }
}
