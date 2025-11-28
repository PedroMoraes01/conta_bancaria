package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PagamentoDTO(String numeroConta, String codigoBoleto,
                           @NotNull(message = "O valor do boleto é obrigatório.")
                           @Positive(message = "O valor deve ser positivo.")
                           BigDecimal valorBoleto) {
}
