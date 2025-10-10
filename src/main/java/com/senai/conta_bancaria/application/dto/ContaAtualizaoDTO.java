package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ContaAtualizaoDTO(
        @NotNull
        BigDecimal saldo,
        @NotNull
        BigDecimal limite,
        @NotNull
        BigDecimal taxa,
        @NotNull
        BigDecimal rendimento
) {
}
