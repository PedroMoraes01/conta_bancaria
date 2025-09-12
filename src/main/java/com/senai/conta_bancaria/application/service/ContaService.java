package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Optional<Conta> buscarContaPorNumero(String numero) {
        return contaRepository.findByNumero(numero);
    }

    @Transactional
    public Conta depositar(String contaDestino, Double valor) {
        if (valor <= 10.00) {
            throw new IllegalArgumentException("Valor de depósito deve ser maior que R$10,00.");
        }

        Conta conta = contaRepository.findByNumero(contaDestino)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

        conta.depositar(valor);
        conta.setSaldo(conta.getSaldo() + valor);
        return contaRepository.save(conta);
    }

    @Transactional
    public Conta sacar(String numeroConta, Double valor) {
        if (valor <= 0.0) {
            throw new IllegalArgumentException("Valor de saque deve ser maior que R$0,00.");
        }

        Conta conta = contaRepository.findByNumero(numeroConta)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

        if (conta.getSaldo() - valor < 0.0) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        conta.sacar(valor);
        conta.setSaldo(conta.getSaldo() - valor);
        return contaRepository.save(conta);
    }

    @Transactional
    public void transferir(String numeroContaOrigem, String numeroContaDestino, Double valor) {
        if (valor <= 0.0) {
            throw new IllegalArgumentException("Valor de transferência deve ser maior que R$0,00.");
        }

        Conta contaOrigem = contaRepository.findByNumero(numeroContaOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));

        Conta contaDestino = contaRepository.findByNumero(numeroContaDestino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));

        if (contaOrigem.getSaldo() - valor < 0.0) {
            throw new IllegalArgumentException("Saldo insuficiente na conta de origem.");
        }

        contaOrigem.transferir(contaDestino, valor);
        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
        contaDestino.setSaldo(contaDestino.getSaldo() + valor);

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
    }
}

