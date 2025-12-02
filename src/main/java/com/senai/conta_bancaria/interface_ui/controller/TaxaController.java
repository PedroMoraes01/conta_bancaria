package com.senai.conta_bancaria.interface_ui.controller;

@RestController
@RequestMapping("/api/taxas")
@RequiredArgsConstructor
@Tag(name = "Taxas", description = "Gestão de taxas financeiras (Apenas Gerentes criam)")

public class TaxaController {
    private final TaxaService service;

    @PostMapping
    @Operation(summary = "Cadastrar nova taxa (ADMIN)", description = "Permite que gerentes cadastrem novas taxas no sistema.")
    public ResponseEntity<Taxa> cadastrarTaxa(@RequestBody @Valid TaxaDTO dto) {
        return ResponseEntity.ok(service.cadastrarTaxa(dto));
    }

    @GetMapping
    @Operation(summary = "Listar taxas", description = "Lista todas as taxas cadastradas.")
    public ResponseEntity<List<Taxa>> listarTaxas() {
        return ResponseEntity.ok(service.listarTaxas());
    }
}
