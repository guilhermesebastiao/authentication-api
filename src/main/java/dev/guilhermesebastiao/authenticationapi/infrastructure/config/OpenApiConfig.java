package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI authenticationApiOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Authentication API")
                                .description("REST API for account registration, authentication, session management, account activation, and password recovery.")
                                .contact(new Contact()
                                        .name("Guilherme Sebastião")
                                        .email("guilhermesebastiaou.u@gmail.com")
                                        .url("https://github.com/guisebastiao/authentication-api")
                                )
                                .license(new License()
                                        .name("MIT License")
                                        .url("https://opensource.org/licenses/MIT")
                                )
                                .version("1.0.0")
                )
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                        .addSecuritySchemes(
                                "sessionAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-Session")
                        )
                );
    }
}
