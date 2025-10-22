package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;

public record ClienteDTO(
        @NotBlank
        String nome,
        @NotBlank
        String email,
        @NotBlank
        String senha,
        @CPF
        String cpf,
        Role role,
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

