package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;

public record ClienteDTO(
        @NotBlank
        String nome,

        @CPF
        String cpf,

        ContaResumoDTO contaDTO
) {
    public Cliente toEntity() {
        return Cliente.builder()
                .nome(this.nome)
                .cpf(this.cpf)
                .ativo(true)
                .contas(new ArrayList<Conta>())
                .build();
    }
}

