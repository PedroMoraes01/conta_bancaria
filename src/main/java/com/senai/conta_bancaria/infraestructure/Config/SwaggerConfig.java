package com.senai.conta_bancaria.infraestructure.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI oficinaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Oficina Mecânica")
                        .description("Cadastro e gestão de serviços de uma oficina.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipe Oficina")
                                .email("suporte@oficina.com"))
                );
    }
}
