package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@SuperBuilder
@DiscriminatorValue("POUPANCA")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContaPoupanca extends Conta {

    @Column(precision = 10, scale = 4)
    private BigDecimal rendimento;


    @Override
    public String getTipo() {
        return "CONTA_POUPANCA";
    }

    public void aplicarRendimento() {
        BigDecimal valorRendimento = this.getSaldo().multiply(rendimento);
        this.setSaldo(this.getSaldo().add(valorRendimento));
    }
}
