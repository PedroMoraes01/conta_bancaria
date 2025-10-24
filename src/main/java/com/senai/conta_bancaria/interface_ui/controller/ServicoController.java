package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ServicoDTO;
import com.senai.conta_bancaria.application.service.ServicoAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Serviços", description = "Gerenciamento de serviços da oficina mecânica")
@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoAppService service;

    public ServicoController(ServicoAppService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cadastrar um novo serviço",
            description = "Adiciona um novo serviço à base de dados após validações de preço e duração",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Serviço cadastrado com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Preço inválido", value = "\"Preço mínimo do serviço deve ser R$ 50,00\""),
                                            @ExampleObject(name = "Duração excedida", value = "\"Duração do serviço não pode exceder 30 dias\"")
                                    }
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ServicoDTO> criar(@Valid @org.springframework.web.bind.annotation.RequestBody ServicoDTO dto) {
        return ResponseEntity
                .status(201)
                .body(service.salvar(dto));
    }

    @Operation(
            summary = "Listar todos os serviços",
            description = "Retorna todos os serviços cadastrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<List<ServicoDTO>>listar() {
        return ResponseEntity
                .ok(service.listar());

    }

    @Operation(
            summary = "Buscar serviço por ID",
            description = "Retorna um serviço existente a partir do seu ID",
            parameters = {
                    @Parameter(name = "id", description = "ID do serviço a ser buscado", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Serviço encontrado"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Serviço não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Serviço com ID 99 não encontrado.\"")
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity
                .ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Atualizar um serviço",
            description = "Atualiza os dados de um serviço existente com novas informações",
            parameters = {
                    @Parameter(name = "id", description = "ID do serviço a ser atualizado", example = "1")
            },

            responses = {
                    @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Preço inválido", value = "\"Preço mínimo do serviço deve ser R$ 50,00\""),
                                            @ExampleObject(name = "Duração excedida", value = "\"Duração do serviço não pode exceder 30 dias\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Serviço não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Serviço com ID 99 não encontrado.\"")
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> atualizar(@PathVariable Long id, @Valid @org.springframework.web.bind.annotation.RequestBody ServicoDTO dto) {
        return ResponseEntity
                .ok(service.atualizar(id, dto));
    }

    @Operation(
            summary = "Deletar um serviço",
            description = "Remove um serviço da base de dados a partir do seu ID",
            parameters = {
                    @Parameter(name = "id", description = "ID do serviço a ser deletado", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Serviço removido com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Serviço não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Serviço com ID 99 não encontrado.\"")
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
