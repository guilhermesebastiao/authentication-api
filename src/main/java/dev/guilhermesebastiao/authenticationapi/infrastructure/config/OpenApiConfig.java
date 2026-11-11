package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiErrorBody;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;
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
                                        .email("contact@guilhermesebastiao.dev")
                                        .url("https://github.com/guilhermesebastiao/authentication-api")
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

    @Bean
    public OpenApiCustomizer errorSchemaCustomizer() {
        return openApi -> SpringDocAnnotationsUtils.extractSchema(
                openApi.getComponents(),
                ApiErrorBody.class,
                null,
                null,
                openApi.getSpecVersion()
        );
    }

    @Bean
    public OperationCustomizer exceptionCustomizer() {
        return (operation, handlerMethod) -> {

            if (operation.getResponses() == null) {
                operation.setResponses(new ApiResponses());
            }

            addError(operation, "400", "Bad Request");
            addError(operation, "401", "Unauthorized");
            addError(operation, "403", "Forbidden");
            addError(operation, "404", "Not Found");
            addError(operation, "409", "Conflict");
            addError(operation, "429", "Too Many Requests");
            addError(operation, "500", "Internal Server Error");
            addError(operation, "502", "Bad Gateway");
            addError(operation, "503", "Service Unavailable");

            return operation;
        };
    }

    private void addError(Operation operation, String status, String description) {
        if (operation.getResponses().containsKey(status)) {
            return;
        }

        operation.getResponses().addApiResponse(
                status,
                new ApiResponse()
                        .description(description)
                        .content(new Content()
                                .addMediaType("application/json", new MediaType()
                                        .schema(new Schema<>()
                                                .$ref("#/components/schemas/ApiErrorBody"))))
        );
    }
}
