package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;

public record ClienteDTO(
        @NotBlank
        @Schema(description = "Nome completo do cliente", example = "João da Silva")
        String nome,
        @NotBlank
        @Schema(description = "Email do cliente (usado para login)", example = "joao.silva@email.com")
        String email,
        @NotBlank
        @Schema(description = "Senha de acesso", example = "senha123")
        String senha,
        @CPF
        @Schema(description = "CPF do cliente (sem pontos ou traços)", example = "12345678900")
        String cpf,
        @Schema(description = "Tipo de usuário (CLIENTE ou ADMIN)", example = "CLIENTE")
        Role role,
        @Schema(description = "Dados da primeira conta a ser criada para o cliente")
        ContaResumoDTO contaDTO
) {
    public Cliente toEntity() {
        return Cliente.builder()
                .nome(this.nome)
                .email(this.email)
                .senha(this.senha)
                .cpf(this.cpf)
                .ativo(true)
                .contas(new ArrayList<Conta>())
                .role(this.role != null ? this.role : Role.CLIENTE)
                .build();
    }

}

