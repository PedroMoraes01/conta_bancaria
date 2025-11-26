package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.domain.entity.Taxa;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final TaxaRepository taxaRepository;
    private final PagamentoService domainService;
    private final IoTService ioTService;

    @Transactional
    public PagamentoResponseDTO realizarPagamento(PagamentoDTO dto) {
        Conta conta = contaRepository.findByNumeroAndAtivaTrue(dto.numeroConta())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta"));

        // 1. Validação IoT
        ioTService.validarCodigoBiometrico(conta.getCliente().getId());

        // 2. Cálculo e Débito (CORREÇÃO AQUI)
        // Buscamos APENAS taxas configuradas para "PAGAMENTO"
        List<Taxa> taxas = taxaRepository.findByTipo(TipoTaxa.PAGAMENTO);

        var valorTotal = domainService.calcularTotal(dto.valorBoleto(), taxas);

        conta.sacar(valorTotal);
        // contaRepository.save(conta); -> Desnecessário se estiver em transação e a entidade for gerenciada, mas mal não faz.

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
        return PagamentoResponseDTO.fromEntity(pag);
    }
}