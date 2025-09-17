package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres")
        Long cpf
) {
        public ClienteDTO fromEntity(Cliente cliente) {
                return new ClienteDTO(
                        cliente.getNome(),
                        cliente.getCpf()
                );
        }
        public Cliente toEntity() {
                Cliente cliente = new Cliente();
                cliente.setNome(this.nome);
                cliente.setCpf(this.cpf);
                return cliente;
        }
}

