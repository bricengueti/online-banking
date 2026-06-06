package com.onlinebank.accountingService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Comptabilité",
                version = "1.0",
                description = "Service REST pour gérer les comptes bancaires et les journaux comptables (crédit, débit, création de compte).",
                contact = @Contact(
                        name = "Brice Tchongoue Ngueti",
                        email = "tchongouebricengueti@gmail.com",
                        url = "https://www.linkedin.com/in/brice-tchongoue"
                ),
                license = @License(
                        name = "Licence MIT",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "/api/accounting", description = "Serveur principal de comptabilité")
        },
        security = {
                @SecurityRequirement(name = "Bearer Authentication")
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Collez ici le token JWT obtenu via /login",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        io.swagger.v3.oas.models.security.SecurityScheme securityScheme =
                new io.swagger.v3.oas.models.security.SecurityScheme()
                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                        .name("Authorization");

        io.swagger.v3.oas.models.security.SecurityRequirement securityRequirement =
                new io.swagger.v3.oas.models.security.SecurityRequirement()
                        .addList("Bearer Authentication");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}
