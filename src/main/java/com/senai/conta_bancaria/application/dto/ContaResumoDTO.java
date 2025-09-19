package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import com.senai.conta_bancaria.domain.entity.ContaPoupanca;

import java.math.BigDecimal;

public record ContaResumoDTO(
        String numero,
        String tipo,
        BigDecimal saldo
) {
    public Conta toEntity(){
    if ("CORRENTE".equalsIgnoreCase(this.tipo))     {
        return ContaCorrente.builder()
                .numero(this.numero)
                .saldo(this.saldo != null ? this.saldo : BigDecimal.ZERO)
                .build();
    } else if ("POUPANCA".equalsIgnoreCase(this.tipo)) {
        return ContaPoupanca.builder()
                .numero(this.numero)
                .saldo(this.saldo != null ? this.saldo : BigDecimal.ZERO)
                .build();
    } else {
        throw new IllegalArgumentException("Tipo de conta inválido: " + this.tipo);
    }
}
