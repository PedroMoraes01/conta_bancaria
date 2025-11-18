package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.PagamentoDTO;
import com.senai.conta_bancaria.application.dto.PagamentoResponseDTO;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import com.senai.conta_bancaria.domain.repository.PagamentoRepository;
import com.senai.conta_bancaria.domain.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoAppService {

    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final TaxaRepository taxaRepository;
    private final PagamentoService domainService;
    private final IoTService ioTService;

    @Transactional
    public PagamentoResponseDTO realizarPagamento(PagamentoDTO dto) {
        Conta conta = contaRepository.findByNumeroAndAtivaTrue(dto.numeroConta())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta"));

        // 1. Validação IoT (Verifica se o código já foi enviado e salvo no banco)
        ioTService.validarCodigoBiometrico(conta.getCliente().getId());

        // 2. Cálculo e Débito
        List<Taxa> taxas = taxaRepository.findAll();
        var valorTotal = domainService.calcularTotal(dto.valorBoleto(), taxas);

        conta.sacar(valorTotal);
        contaRepository.save(conta);

        // 3. Persistência
        Pagamento pag = Pagamento.builder()
                .conta(conta)
                .boleto(dto.codigoBoleto())
                .valorPago(valorTotal)
                .dataPagamento(LocalDateTime.now())
                .status("SUCESSO")
                .taxas(taxas)
                .build();

        pagamentoRepository.save(pag);
        return PagamentoResponseDTO.fromEntity(pag); // Certifique-se de ter o método fromEntity no DTO
    }
}
