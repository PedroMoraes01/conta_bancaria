package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public class ContaCorrente extends Conta {


    private double limite;


    private Double taxa;


    @Override
    public void depositar(Double limite) {

    }

    @Override
    public void sacar(Double valor) {

    }

    @Override
    public void transferir(Conta contaDestino, Double valor) {

    }
}
