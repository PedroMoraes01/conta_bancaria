package com.senai.conta_bancaria.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthDTO {

    public record LoginRequest(
            @Schema(description = "Email cadastrado", example = "admin@senai.com")
            String email,
            @Schema(description = "Senha cadastrada", example = "admin123")
            String senha
    ) {}
    public record TokenResponse(
            @Schema(description = "Token JWT de autenticação")
            String token
    ) {}
}
