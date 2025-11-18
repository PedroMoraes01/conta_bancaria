package com.senai.conta_bancaria.application.dto;

import lombok.Builder;

@Builder
public record SolicitacaoAuthDTO(String clienteId, String mensagem) {
}
