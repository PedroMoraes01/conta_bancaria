package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ClienteDTO;
import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.application.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @Operation(
            summary = "Registrar novo cliente ou anexar conta",
            description = "Cria um novo cliente (se o CPF não existir) e anexa uma nova conta (Corrente ou Poupança). Se o cliente já existir, apenas anexa a nova conta, desde que seja de um tipo diferente das existentes.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do cliente e da conta inicial",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente registrado ou conta anexada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação (ex: CPF inválido, tipo de conta inválido)"),
                    @ApiResponse(responseCode = "409", description = "Cliente já possui uma conta do mesmo tipo",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"title\":\"Conflito\",\"detail\":\"O Cliente já possui uma conta do mesmo tipo.\"}"))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@RequestBody ClienteDTO dto) {
        ClienteResponseDTO novoCliente = service.registrarClienteOuAnexarConta(dto);

        return ResponseEntity.created(
                URI.create("api/clientes/cpf" + novoCliente.cpf())
        ).body(novoCliente);
    }

    @Operation(
            summary = "Listar clientes ativos",
            description = "Retorna uma lista de todos os clientes que estão com status 'ativo'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientesAtivos() {
        return ResponseEntity.ok(service.listarClientesAtivos());
    }

    @Operation(
            summary = "Buscar cliente por CPF",
            description = "Retorna os dados de um cliente específico e suas contas, com base no CPF (apenas clientes ativos)",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser buscado", example = "12345678900", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                    @ApiResponse(responseCode = "404", description = "Cliente não existente ou inativo",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"title\":\"Não encontrado\",\"detail\":\"Cliente não existente ou inativo(a)!\"}"))
                    )
            }
    )
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> buscarClienteAtivoPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarClienteAtivoPorCpf(cpf));
    }

    @Operation(
            summary = "Atualizar dados do cliente",
            description = "Atualiza nome, email, cpf e senha de um cliente existente",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser atualizado", example = "12345678900", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do cliente para atualização",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não existente ou inativo")
            }
    )
    @PutMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable String cpf,
                                                               @RequestBody ClienteDTO dto){
        return ResponseEntity.ok(service.atualizarCliente(cpf,dto));
    }

    @Operation(
            summary = "Desativar (deletar) cliente",
            description = "Realiza a exclusão lógica de um cliente, marcando-o como 'inativo' e desativando todas as suas contas associadas.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser desativado", example = "12345678900", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente desativado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não existente ou inativo")
            }
    )
    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String cpf){
        service.deletarCliente(cpf);
        return ResponseEntity.noContent().build();
    }



}