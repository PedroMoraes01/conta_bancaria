package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;


@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Optional<Conta> buscarContaPorNumero(String numero) {
        return contaRepository.findByNumero(numero);
    }

    @Transactional
   public Conta depositar(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada: " + numeroConta));
        conta.depositar(valor);
        return contaRepository.save(conta);
    }

    @Transactional
    public Conta sacar(String numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada: " + numeroConta));
        conta.sacar(valor);
        return contaRepository.save(conta);
    }

    @Transactional
    public void transferir(String numeroContaOrigem, String numeroContaDestino, BigDecimal valor) {
        Conta contaOrigem = buscarContaPorNumero(numeroContaOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada: " + numeroContaOrigem));
        Conta contaDestino = buscarContaPorNumero(numeroContaDestino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada: " + numeroContaDestino));

        contaOrigem.sacar(valor);
        contaDestino.depositar(valor);

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
    }
}

