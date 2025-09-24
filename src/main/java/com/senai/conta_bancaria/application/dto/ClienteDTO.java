package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;

import java.util.ArrayList;

public record ClienteDTO(
        String nome,
        String cpf,
        ContaResumoDTO contaDTO
) {
    public Cliente toEntity(){
        return Cliente.builder()
                .nome(this.nome)
                .cpf(this.cpf)
                .ativo(true)
                .contas(new ArrayList<Conta>())
                .build();
    }
}

