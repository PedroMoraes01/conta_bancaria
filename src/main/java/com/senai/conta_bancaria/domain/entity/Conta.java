package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Conta {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private int numero;
    private double saldo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public void depositar(Double valor) {
        if (valor > 0) {
            saldo += valor;
        } else {
            throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        }
    }

    public void sacar(Double valor) {
        if (valor > 0 && valor <= saldo) {
            saldo -= valor;
        } else {
            throw new IllegalArgumentException("Saldo insuficiente ou valor inválido.");
        }
    }

    public void transferir(Conta contaDestino, Double valor) {
        if (valor > 0 && valor <= saldo) {
            this.sacar(valor);
            contaDestino.depositar(valor);
        } else {
            throw new IllegalArgumentException("Saldo insuficiente ou valor inválido para transferência.");
        }
    }
}
