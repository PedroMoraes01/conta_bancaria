package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.TaxaDTO;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.repository.TaxaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxaService {
    private final TaxaRepository repository;

    // Apenas ADMIN (Gerentes) podem cadastrar taxas conforme o PDF
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Taxa cadastrarTaxa(TaxaDTO dto) {
        return repository.save(dto.toEntity());
    }

    // Clientes e Admins podem visualizar as taxas para entender as cobranças
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public List<Taxa> listarTaxas() {
        return repository.findAll();
    }
}
