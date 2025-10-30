package com.app.budgets.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Budgets App: API Documentation")
                        .description("Budgets App: API Documentation")
                )
                .servers(List.of(
                                new Server().url("http://localhost:8080/api/v1").description("local"),
                                new Server().url("http://localhost:8080/api/v2").description("local")
                        )
                )
                .tags(Arrays.asList(
                        new Tag().name("Auth"),
                        new Tag().name("Budget Category"),
                        new Tag().name("Budget"),
                        new Tag().name("Recurring Budget")
                ))
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"))
                .components(new Components()
                        .addSecuritySchemes("cookieAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("access_token")));
    }

}
