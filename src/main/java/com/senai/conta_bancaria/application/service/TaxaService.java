package com.senai.conta_bancaria.application.service;

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
