package com.senai.conta_bancaria.application.dto;

import java.math.BigDecimal;

public record ContaAtualizaoDTO(
        BigDecimal saldo,

        BigDecimal limite,

        BigDecimal taxa,

        BigDecimal rendimento
) {
}
