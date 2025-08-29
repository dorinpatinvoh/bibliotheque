package com.example.bibliotheque.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "basicAuth"; // Nom que nous donnons à notre schéma de sécurité

        return new OpenAPI()
                // --- PARTIE 1 : DÉFINIR LE SCHÉMA DE SÉCURITÉ ---
                // On explique à Swagger que l'on utilise HTTP Basic.
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic") // Le type d'authentification HTTP
                                        .description("Entrez le nom d'utilisateur et le mot de passe.")
                        )
                )
                // --- PARTIE 2 : APPLIQUER CE SCHÉMA GLOBALEMENT ---
                // On dit à Swagger d'appliquer ce schéma à tous les endpoints.
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // --- Informations générales de l'API (ce que vous aviez déjà) ---
                .info(new Info()
                        .title("API de Gestion de Bibliothèque 📚")
                        .version("v1.0")
                        .description("Une API REST développée avec Spring Boot pour gérer les livres, les utilisateurs et les emprunts d'une bibliothèque.")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}