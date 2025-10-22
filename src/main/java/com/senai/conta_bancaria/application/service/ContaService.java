package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ContaAtualizaoDTO;
import com.senai.conta_bancaria.application.dto.ContaResumoDTO;
import com.senai.conta_bancaria.application.dto.TransferenciaDTO;
import com.senai.conta_bancaria.application.dto.ValorSaqueDepositoDTO;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import com.senai.conta_bancaria.domain.entity.ContaPoupanca;
import com.senai.conta_bancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {
    private final ContaRepository repository;

    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<ContaResumoDTO> listarTodasContas() {
        return repository.findAllByAtivaTrue().stream()
                .map(ContaResumoDTO::fromEntity).toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional(readOnly = true)
    public ContaResumoDTO buscarContaPorNumero(String numeroDaConta) {
        return ContaResumoDTO.fromEntity(
                repository.findByNumeroAndAtivaTrue(numeroDaConta)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta"))
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ContaResumoDTO atualizarConta(String numeroConta, ContaAtualizaoDTO dto) {
        var conta = buscaContaAtivaPorNumero(numeroConta);
        conta.setSaldo(dto.saldo());

        if (conta instanceof ContaPoupanca poupanca){
            poupanca.setRendimento(dto.rendimento());
        }else if (conta instanceof ContaCorrente corrente){
            corrente.setLimite(dto.limite());
            corrente.setTaxa(dto.taxa());
        }
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    private Conta buscaContaAtivaPorNumero(String numeroConta) {
        return repository.findByNumeroAndAtivaTrue(numeroConta).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Conta")
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public void deletarConta(String numeroDaConta) {
        var conta = buscaContaAtivaPorNumero(numeroDaConta);
        conta.setAtiva(false);
        repository.save(conta);
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO sacar(String numeroDaConta, ValorSaqueDepositoDTO dto) {
        var conta = buscaContaAtivaPorNumero(numeroDaConta);
        conta.sacar(dto.valor());
        conta.setSaldo(conta.getSaldo().subtract(dto.valor()));
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO depositar(String numeroDaConta, ValorSaqueDepositoDTO dto) {
        var conta = buscaContaAtivaPorNumero(numeroDaConta);
        conta.depositar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO transferir(String numeroDaConta, TransferenciaDTO dto) {
        var contaOrigem = buscaContaAtivaPorNumero(numeroDaConta);
        var contaDestino = buscaContaAtivaPorNumero(dto.contaDestino());

        contaOrigem.sacar(dto.valor());
        contaDestino.depositar(dto.valor());

        contaOrigem.transferir(dto.valor(), contaDestino);

        repository.save(contaDestino);
        return ContaResumoDTO.fromEntity(repository.save(contaOrigem));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ContaResumoDTO aplicarRendimento(String numeroDaConta) {
        var conta = buscaContaAtivaPorNumero(numeroDaConta);
        if (conta instanceof ContaPoupanca poupanca){
            poupanca.aplicarRendimento();
            return ContaResumoDTO.fromEntity(repository.save(poupanca));
        } else throw new EntidadeNaoEncontradaException("Conta Poupança");

    }
}


