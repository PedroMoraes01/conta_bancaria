package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import com.senai.conta_bancaria.domain.entity.ContaPoupanca;
import com.senai.conta_bancaria.domain.exceptions.TipoDeContaInvalidaException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ContaResumoDTO(
        @NotBlank
        String numero,

        @NotBlank
        String tipo,

        @NotNull
        BigDecimal saldo
) {
    public Conta toEntity(Cliente cliente) {
        if ("CORRENTE".equalsIgnoreCase(this.tipo)) {
            return ContaCorrente.builder()
                    .numero(this.numero)
                    .saldo(this.saldo != null ? this.saldo : BigDecimal.ZERO)
                    .cliente(cliente)
                    .ativa(true)
                    .build();
        } else if ("POUPANCA".equalsIgnoreCase(this.tipo)) {
            return ContaPoupanca.builder()
                    .numero(this.numero)
                    .saldo(this.saldo != null ? this.saldo : BigDecimal.ZERO)
                    .ativa(true)
                    .cliente(cliente)
                    .build();
        } else {
            throw new TipoDeContaInvalidaException();
        }
    }

    public static ContaResumoDTO fromEntity(Conta c ){
        return new ContaResumoDTO(
                c.getNumero(),
                c.getTipo(),
                c.getSaldo()
        );
    }
}