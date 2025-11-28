package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.PagamentoDTO;
import com.senai.conta_bancaria.application.dto.PagamentoResponseDTO;
import com.senai.conta_bancaria.application.service.PagamentoAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Gestão financeira")
public class PagamentoController {

    private final PagamentoAppService service;

    @PostMapping
    @Operation(summary = "Efetuar Pagamento", description = "Realiza o débito se houver autenticação IoT válida recente.")
    public ResponseEntity<PagamentoResponseDTO> pagar(@RequestBody @Valid PagamentoDTO dto) {
        return ResponseEntity.ok(service.realizarPagamento(dto));
    }
}
