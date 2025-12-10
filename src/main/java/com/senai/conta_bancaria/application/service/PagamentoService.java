package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.PagamentoDTO;
import com.senai.conta_bancaria.application.dto.PagamentoResponseDTO;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.enums.TipoTaxa;
import com.senai.conta_bancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.exceptions.ValidacaoException;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import com.senai.conta_bancaria.domain.repository.PagamentoRepository;
import com.senai.conta_bancaria.domain.repository.TaxaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {
    public BigDecimal calcularTotal(BigDecimal valorOriginal, List<Taxa> taxas) {
        if (valorOriginal == null) {
            throw new ValidacaoException("O valor do boleto é obrigatório e deve ser numérico.");
        }

        BigDecimal total = valorOriginal;

        for (Taxa taxa : taxas) {
            if (taxa.getValorFixo() != null) {
                total = total.add(taxa.getValorFixo());
            }
            if (taxa.getPercentual() != null) {
                BigDecimal valorTaxa = valorOriginal.multiply(taxa.getPercentual());
                total = total.add(valorTaxa);
            }
        }

        return total;
    }

}