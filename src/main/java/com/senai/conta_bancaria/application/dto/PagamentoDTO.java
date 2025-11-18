package com.senai.conta_bancaria.application.dto;

import java.math.BigDecimal;

public record PagamentoDTO(String numeroConta, String codigoBoleto, BigDecimal valorBoleto) {
}
