package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.PagamentoDTO;
import com.senai.conta_bancaria.application.dto.PagamentoResponseDTO;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.enums.TipoTaxa;
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
    // --- 1. Injeção dos Repositórios (Resolvendo os erros vermelhos) ---
    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final TaxaRepository taxaRepository;

    // --- 2. Injeção dos Serviços de Apoio ---
    private final PagamentoService domainService; // Serviço de Cálculo (Matemática)
    private final IoTService ioTService;          // Serviço de Segurança (Biometria)

    @Transactional
    public PagamentoResponseDTO realizarPagamento(PagamentoDTO dto) {

        // --- 1. Busca da Conta ---
        // Correção: Usando 'contaRepository' (variável) em vez de 'ContaRepository' (classe)
        Conta conta = contaRepository.findByNumeroAndAtivaTrue(dto.numeroConta())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta"));

        // --- 2. Validação de Segurança IoT ---
        // Verifica se houve autenticação biométrica recente (requisito do PDF)
        ioTService.validarCodigoBiometrico(conta.getCliente().getId());

        // --- 3. Busca de Taxas ---
        // Correção: Buscando apenas taxas do tipo PAGAMENTO
        List<Taxa> taxas = taxaRepository.findByTipo(TipoTaxa.PAGAMENTO);

        // --- 4. Cálculo do Valor Total ---
        // Correção: Usando o serviço de domínio para somar taxas + boleto
        var valorTotal = domainService.calcularTotal(dto.valorBoleto(), taxas);

        // --- 5. Débito na Conta ---
        // O método sacar() valida saldo insuficiente automaticamente
        conta.sacar(valorTotal);
        contaRepository.save(conta);

        // --- 6. Registro do Pagamento (Persistência) ---
        Pagamento pag = Pagamento.builder()
                .conta(conta)
                .boleto(dto.codigoBoleto())
                .valorPago(valorTotal) // Valor final com taxas inclusas
                .dataPagamento(LocalDateTime.now())
                .status("SUCESSO")
                .taxas(taxas)
                .build();

        pagamentoRepository.save(pag);

        // --- 7. Retorno do DTO ---
        return PagamentoResponseDTO.fromEntity(pag);
    }


}
