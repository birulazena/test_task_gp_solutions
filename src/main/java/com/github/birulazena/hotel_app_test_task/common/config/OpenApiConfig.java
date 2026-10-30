package com.github.birulazena.hotel_app_test_task.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
                        .title("Hotel Property View API")
                        .version("1.0.0")
                        .description("""
                                ### RESTful API for hotel management and analytics
                                
                                The service provides the following capabilities:
                                * Get a brief overview list of all hotels
                                * Retrieve detailed information for a specific hotel
                                * Search hotels by criteria (name, brand, city, country, amenities)
                                * Create new hotels
                                * Add amenities to an existing hotel
                                * Generate histograms grouped by parameters (brand, city, country, amenities)
                                """)
                        .contact(new Contact()
                                .name("birulazena")
                                .url("https://github.com/birulazena")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("/property-view")
                                .description("Default Server URL")
                ));
    }
}