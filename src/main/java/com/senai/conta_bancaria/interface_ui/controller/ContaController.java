package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ContaAtualizaoDTO;
import com.senai.conta_bancaria.application.dto.ContaResumoDTO;
import com.senai.conta_bancaria.application.dto.TransferenciaDTO;
import com.senai.conta_bancaria.application.dto.ValorSaqueDepositoDTO;
import com.senai.conta_bancaria.application.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/conta")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService service;

    @Operation(
            summary = "Listar todas as contas ativas (ADMIN)",
            description = "Retorna uma lista de todas as contas ativas no sistema. Requer permissão de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer ADMIN)")
            }
    )
    @GetMapping
    public ResponseEntity<List<ContaResumoDTO>> listarTodasContas() {
        return ResponseEntity.ok(service.listarTodasContas());
    }

    @Operation(
            summary = "Buscar conta por número (ADMIN)",
            description = "Retorna os dados de uma conta específica pelo seu número. Requer permissão de ADMIN.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta", example = "12345-6", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta encontrada"),
                    @ApiResponse(responseCode = "404", description = "Conta não existente ou inativa"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer ADMIN)")
            }
    )
    @GetMapping("/{numeroDaConta}")
    public ResponseEntity<ContaResumoDTO> buscarContaPorNumero(@PathVariable String numeroDaConta) {
        return ResponseEntity.ok(service.buscarContaPorNumero(numeroDaConta));
    }

    @Operation(
            summary = "Atualizar dados da conta (ADMIN/CLIENTE)",
            description = "Atualiza saldo, limite (Conta Corrente) ou rendimento (Conta Poupança).",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta", example = "12345-6", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados de atualização",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ContaAtualizaoDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não existente ou inativa")
            }
    )
    @PutMapping("/{numeroDaConta}")
    public ResponseEntity<ContaResumoDTO> atualizarConta(
            @PathVariable String numeroDaConta,
            @RequestBody ContaAtualizaoDTO dto) {
        return ResponseEntity.ok(service.atualizarConta(numeroDaConta, dto));
    }

    @Operation(
            summary = "Desativar (deletar) conta (ADMIN/CLIENTE)",
            description = "Realiza a exclusão lógica de uma conta, marcando-a como 'inativa'.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta", example = "12345-6", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Conta desativada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não existente ou inativa")
            }
    )
    @DeleteMapping("/{numeroDaConta}")
    public ResponseEntity<Void> deletarConta(@PathVariable String numeroDaConta) {
        service.deletarConta(numeroDaConta);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Realizar saque (CLIENTE)",
            description = "Subtrai um valor do saldo da conta. Para Conta Corrente, considera o limite. Requer permissão de CLIENTE.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta de origem", example = "12345-6", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Valor a ser sacado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ValorSaqueDepositoDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou valor negativo"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer CLIENTE)")
            }
    )
    @PostMapping("/{numeroDaConta}/sacar")
    public ResponseEntity<ContaResumoDTO> sacar(
            @PathVariable String numeroDaConta, @Valid @RequestBody ValorSaqueDepositoDTO dto) {
        return ResponseEntity.ok(service.sacar(numeroDaConta, dto));
    }

    @Operation(
            summary = "Realizar depósito (CLIENTE)",
            description = "Adiciona um valor ao saldo da conta. Requer permissão de CLIENTE.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta de destino", example = "12345-6", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Valor a ser depositado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ValorSaqueDepositoDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Valor negativo"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer CLIENTE)")
            }
    )
    @PostMapping("/{numeroDaConta}/depositar")
    public ResponseEntity<ContaResumoDTO> depositar(
            @PathVariable String numeroDaConta, @Valid @RequestBody ValorSaqueDepositoDTO dto) {
        return ResponseEntity.ok(service.depositar(numeroDaConta, dto));
    }

    @Operation(
            summary = "Realizar transferência (CLIENTE)",
            description = "Transfere um valor de uma conta de origem para uma conta de destino. Requer permissão de CLIENTE.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta de ORIGEM", example = "12345-6", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Valor e conta de destino",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransferenciaDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente, valor negativo ou transferência para a mesma conta"),
                    @ApiResponse(responseCode = "404", description = "Conta de origem ou destino não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer CLIENTE)")
            }
    )
    @PostMapping("/{numeroDaConta}/transferir/")
    public ResponseEntity<ContaResumoDTO> transferir(
            @PathVariable String numeroDaConta,
            @RequestBody TransferenciaDTO valor) {
        return ResponseEntity.ok(service.transferir(numeroDaConta, valor));
    }

    @Operation(
            summary = "Aplicar rendimento (ADMIN)",
            description = "Aplica o rendimento definido em uma Conta Poupança. Falha se a conta não for Poupança. Requer permissão de ADMIN.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da Conta Poupança", example = "54321-0", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rendimento aplicado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta Poupança não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer ADMIN)")
            }
    )
    @PostMapping("/{numeroDaConta}/rendimento")
    public ResponseEntity<ContaResumoDTO> aplicarRendimento(
            @PathVariable String numeroDaConta) {
        return ResponseEntity.ok(service.aplicarRendimento(numeroDaConta));
    }
}