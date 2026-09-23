package com.example.scaffold.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI scaffoldOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Java Backend Scaffold API")
                        .description("Spring Boot 3 scaffold with layered structure, unified responses, and sample User CRUD")
                        .version("0.1.0"));
    }
}
