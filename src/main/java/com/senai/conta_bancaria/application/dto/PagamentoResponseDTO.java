package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoResponseDTO(
        String id,
        String numeroConta,
        String codigoBoleto,
        BigDecimal valorPago, // Valor final com taxas
        LocalDateTime dataPagamento,
        String status,
        List<String> taxasAplicadas // Retorna apenas os nomes das taxas para simplificar
) {

    // Método estático para converter a Entidade em DTO
    public static PagamentoResponseDTO fromEntity(Pagamento pagamento) {
        List<String> nomesTaxas = pagamento.getTaxas().stream()
                .map(Taxa::getDescricao)
                .toList();

        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getConta().getNumero(),
                pagamento.getBoleto(),
                pagamento.getValorPago(),
                pagamento.getDataPagamento(),
                pagamento.getStatus(),
                nomesTaxas
        );
    }
}
