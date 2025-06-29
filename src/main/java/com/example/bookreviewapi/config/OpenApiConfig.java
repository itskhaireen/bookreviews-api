package com.example.bookreviewapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Review API")
                        .version("1.0.0")
                        .description("A comprehensive REST API for managing books and reviews with full CRUD operations, validation, and exception handling.")
                        .contact(new Contact()
                                .name("Book Review API Team")
                                .email("contact@bookreviewapi.com")
                                .url("https://github.com/itskhaireen/bookreviews-api")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development server"),
                        new Server().url("https://api.bookreview.com").description("Production server")
                ));
    }
} 