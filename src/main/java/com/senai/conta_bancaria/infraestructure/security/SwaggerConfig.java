package com.senai.conta_bancaria.infraestructure.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI contaBancariaOpenAPI() { // Nome do método atualizado
        return new OpenAPI()
                .info(new Info()
                        .title("API - Conta Bancária") // Título atualizado
                        .description("API para gestão de clientes e contas bancárias.") // Descrição atualizada
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipe Conta Bancária") // Contato atualizado
                                .email("suporte@contabancaria.com")) // Contato atualizado
                );
    }
}
